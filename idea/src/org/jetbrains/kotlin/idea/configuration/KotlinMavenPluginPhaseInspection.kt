/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.configuration

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.highlighter.JavaFileType
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomFileElement
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder
import com.intellij.util.xml.highlighting.DomElementsInspection
import org.jetbrains.idea.maven.dom.model.MavenDomGoal
import org.jetbrains.idea.maven.dom.model.MavenDomPlugin
import org.jetbrains.idea.maven.dom.model.MavenDomPluginExecution
import org.jetbrains.idea.maven.dom.model.MavenDomProjectModel
import org.jetbrains.idea.maven.model.MavenId
import org.jetbrains.idea.maven.model.MavenPlugin
import org.jetbrains.idea.maven.project.MavenProjectsManager
import org.jetbrains.idea.maven.utils.MavenArtifactScope
import java.util.*

class KotlinMavenPluginPhaseInspection : DomElementsInspection<MavenDomProjectModel>(MavenDomProjectModel::class.java) {
    override fun getStaticDescription() = "The inspecition's purpose is to check Maven pom and kotlin maven plugin configuration"

    override fun checkFileElement(domFileElement: DomFileElement<MavenDomProjectModel>?, holder: DomElementAnnotationHolder?) {
        if (domFileElement == null || holder == null) {
            return
        }

        val module = domFileElement.module ?: return
        val manager = MavenProjectsManager.getInstance(module.project)
        val mavenProject = manager.findProject(module) ?: return

        val project = domFileElement.rootElement
        val kotlinPlugin = project.build.plugins.plugins.firstOrNull { it.isKotlinMavenPlugin() } ?: return
        val hasJavaFiles = module.hasJavaFiles()

        val executions = mavenProject.plugins
                .filter { it.isKotlinMavenPlugin() }
                .flatMap { it.executions }

        val allGoalsSet: Set<String> = executions.flatMapTo(HashSet()) { it.goals }

        if (PomFile.KotlinGoals.Compile !in allGoalsSet && PomFile.KotlinGoals.Js !in allGoalsSet) {
            val fixes = if (hasJavaFiles) {
                arrayOf(AddExecutionLocalFix(domFileElement.file, module, kotlinPlugin, PomFile.KotlinGoals.Compile))
            }
            else {
                arrayOf(AddExecutionLocalFix(domFileElement.file, module, kotlinPlugin, PomFile.KotlinGoals.Compile),
                        AddExecutionLocalFix(domFileElement.file, module, kotlinPlugin, PomFile.KotlinGoals.Js))
            }

            holder.createProblem(kotlinPlugin.artifactId.createStableCopy(),
                                 HighlightSeverity.WARNING,
                                 "Kotlin plugin has no compile executions",
                                 *fixes)
        }
        else if (hasJavaFiles) {
            kotlinPlugin.executions.executions.filter { it.phase.stringValue != PomFile.DefaultPhases.ProcessSources && it.hasGoal(PomFile.KotlinGoals.Compile) }.forEach { badExecution ->
                holder.createProblem(badExecution.phase.createStableCopy(),
                                     HighlightSeverity.WARNING,
                                     "Kotlin plugin should run before javac so kotlin classes could be visible from Java",
                                     FixExecutionPhaseLocalFix(badExecution, PomFile.DefaultPhases.ProcessSources))
            }

            kotlinPlugin.executions.executions.filter { it.hasGoal(PomFile.KotlinGoals.Js) || it.hasGoal(PomFile.KotlinGoals.TestJs) }.forEach { badExecution ->
                holder.createProblem(badExecution.goals.goals.first { it.isJsGoal() }.createStableCopy(),
                                     HighlightSeverity.WARNING,
                                     "JavaScript goal configured for module with Java files")
            }
        }
        else {
            val stdlibDependencies  = mavenProject.findDependencies(KotlinJavaMavenConfigurator.GROUP_ID, KotlinJavaMavenConfigurator.STD_LIB_ID)
            val jsDependencies  = mavenProject.findDependencies(KotlinJavaMavenConfigurator.GROUP_ID, KotlinJavascriptMavenConfigurator.STD_LIB_ID)

            val hasJvmExecution = PomFile.KotlinGoals.Compile in allGoalsSet || PomFile.KotlinGoals.TestCompile in allGoalsSet
            val hasJsExecution = PomFile.KotlinGoals.Js in allGoalsSet || PomFile.KotlinGoals.TestJs in allGoalsSet

            if (hasJvmExecution && stdlibDependencies.isEmpty()) {
                holder.createProblem(kotlinPlugin.artifactId.createStableCopy(),
                                     HighlightSeverity.WARNING,
                                     "Kotlin JVM compiler configured but no ${KotlinJavaMavenConfigurator.STD_LIB_ID} dependency",
                                     FixAddStdlibLocalFix(domFileElement.file, KotlinJavaMavenConfigurator.STD_LIB_ID, kotlinPlugin.version.rawText))
            }
            if (hasJsExecution && jsDependencies.isEmpty()) {
                holder.createProblem(kotlinPlugin.artifactId.createStableCopy(),
                                     HighlightSeverity.WARNING,
                                     "Kotlin JavaScript compiler configured but no ${KotlinJavascriptMavenConfigurator.STD_LIB_ID} dependency",
                                     FixAddStdlibLocalFix(domFileElement.file, KotlinJavascriptMavenConfigurator.STD_LIB_ID, kotlinPlugin.version.rawText))
            }
        }
    }

    private class AddExecutionLocalFix(val file: XmlFile, val module: Module, val kotlinPlugin: MavenDomPlugin, val goal: String) : LocalQuickFix {
        override fun getName() = "Create $goal execution"

        override fun getFamilyName() = "Kotlin"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val pom = PomFile(file)

            pom.addKotlinExecution(module, kotlinPlugin, goal, PomFile.getPhase(module.hasJavaFiles(), false), false, listOf(goal))
        }
    }

    private class FixExecutionPhaseLocalFix(val execution: MavenDomPluginExecution, val newPhase: String) : LocalQuickFix {
        override fun getName() = "Change phase to $newPhase"

        override fun getFamilyName() = "Kotlin"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            execution.phase.value = newPhase
        }
    }

    private class FixAddStdlibLocalFix(val pomFile: XmlFile, val id: String, val version: String?) : LocalQuickFix {
        override fun getName() = "Add $id dependency"
        override fun getFamilyName() = "Kotlin"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val file = PomFile(pomFile)
            file.addDependency(MavenId(KotlinJavaMavenConfigurator.GROUP_ID, id, version), MavenArtifactScope.COMPILE)
        }
    }
}

private fun Module.hasJavaFiles(): Boolean {
    return FileTypeIndex.containsFileOfType(JavaFileType.INSTANCE, GlobalSearchScope.moduleScope(this))
}

private fun MavenDomPlugin.isKotlinMavenPlugin() = groupId.stringValue == KotlinMavenConfigurator.GROUP_ID
                                                   && artifactId.stringValue == KotlinMavenConfigurator.MAVEN_PLUGIN_ID

private fun MavenPlugin.isKotlinMavenPlugin() = groupId == KotlinMavenConfigurator.GROUP_ID
                                                && artifactId == KotlinMavenConfigurator.MAVEN_PLUGIN_ID

private fun MavenDomGoal.isJsGoal() = rawText == PomFile.KotlinGoals.Js || rawText == PomFile.KotlinGoals.TestJs

private fun MavenDomPluginExecution.hasGoal(goal: String) = goals.goals.any { it.rawText == goal }
