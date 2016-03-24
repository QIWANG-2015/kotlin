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

package org.jetbrains.kotlin.idea.maven

import com.intellij.analysis.AnalysisScope
import com.intellij.codeInspection.*
import com.intellij.codeInspection.ex.InspectionManagerEx
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.testFramework.InspectionTestUtil
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.kotlin.idea.configuration.KotlinMavenPluginPhaseInspection
import org.jetbrains.kotlin.idea.refactoring.toPsiDirectory
import org.jetbrains.kotlin.idea.util.projectStructure.allModules
import java.io.File

class KotlinMavenInspectionTest : MavenImportingTestCase() {

    override fun setUp() {
        super.setUp()
        repositoryPath = File(myDir, "repo").path
        createStdProjectFolders()
    }

    fun testNoExecutions() {
        importProject("""
            <groupId>org.jetbrains.kotlin.test</groupId>
            <artifactId>configure-maven-test</artifactId>
            <version>1.0-SNAPSHOT</version>

            <properties>
                <kotlin.version>1.0.1</kotlin.version>
            </properties>

            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>${'$'}{kotlin.version}</version>
                    </plugin>
                </plugins>
            </build>
        """.replaceIndent("    "))
        setupJdkForModule(myProject.allModules()[0].name)

        runInspectionAndAssert<KotlinMavenPluginPhaseInspection>(listOf(
                SimplifiedProblemDescription("Kotlin plugin has no compile executions", "kotlin-maven-plugin", listOf("Create compile execution", "Create js execution"))
        )) { problems ->
            problems.single().applyFixNumber(0)
            // TODO quickfix
        }
    }

    fun testWrongJsExecution() {
        importProject("""
            <groupId>org.jetbrains.kotlin.test</groupId>
            <artifactId>configure-maven-test</artifactId>
            <version>1.0-SNAPSHOT</version>

            <properties>
                <kotlin.version>1.0.1</kotlin.version>
            </properties>

            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>${'$'}{kotlin.version}</version>
                        <executions>
                            <execution>
                                <phase>compile</phase>
                                <goals>
                                    <goal>js</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        """.replaceIndent("    "))
        setupJdkForModule(myProject.allModules()[0].name)
        mkJavaFile()

        runInspectionAndAssert<KotlinMavenPluginPhaseInspection>(listOf(
                SimplifiedProblemDescription("JavaScript goal configured for module with Java files", "js", listOf())
        )) {
        }
    }

    fun testWrongPhaseExecution() {
        importProject("""
            <groupId>org.jetbrains.kotlin.test</groupId>
            <artifactId>configure-maven-test</artifactId>
            <version>1.0-SNAPSHOT</version>

            <properties>
                <kotlin.version>1.0.1</kotlin.version>
            </properties>

            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>${'$'}{kotlin.version}</version>
                        <executions>
                            <execution>
                                <phase>compile</phase>
                                <goals>
                                    <goal>compile</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        """.replaceIndent("    "))
        setupJdkForModule(myProject.allModules()[0].name)
        mkJavaFile()

        runInspectionAndAssert<KotlinMavenPluginPhaseInspection>(listOf(
                SimplifiedProblemDescription("Kotlin plugin should run before javac so kotlin classes could be visible from Java", "compile", listOf("Change phase to process-sources"))
        )) { problems ->
            problems.single().applyTheOnlyFix()
            // TODO quickfix
        }
    }

    fun testMissingDependencies() {
        importProject("""
            <groupId>org.jetbrains.kotlin.test</groupId>
            <artifactId>configure-maven-test</artifactId>
            <version>1.0-SNAPSHOT</version>

            <properties>
                <kotlin.version>1.0.1</kotlin.version>
            </properties>

            <build>
                <plugins>
                    <plugin>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-plugin</artifactId>
                        <version>${'$'}{kotlin.version}</version>
                        <executions>
                            <execution>
                                <phase>compile</phase>
                                <goals>
                                    <goal>compile</goal>
                                    <goal>js</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        """.replaceIndent("    "))
        setupJdkForModule(myProject.allModules()[0].name)

        runInspectionAndAssert<KotlinMavenPluginPhaseInspection>(listOf(
                SimplifiedProblemDescription("Kotlin JVM compiler configured but no kotlin-stdlib dependency", "kotlin-maven-plugin", listOf("Add kotlin-stdlib dependency")),
                SimplifiedProblemDescription("Kotlin JavaScript compiler configured but no kotlin-js-library dependency", "kotlin-maven-plugin", listOf("Add kotlin-js-library dependency"))
        )) { problems ->
            problems[0].applyTheOnlyFix()
        }
    }

    private fun ProblemDescriptorBase.applyTheOnlyFix() {
        fixes!!.single().apply(this)
    }

    private fun ProblemDescriptorBase.applyFixNumber(n: Int) {
        fixes!![n].apply(this)
    }

    private fun QuickFix<CommonProblemDescriptor>.apply(desc: ProblemDescriptorBase) {
        CommandProcessor.getInstance().executeCommand(myProject, {
            ApplicationManager.getApplication().runWriteAction {
                applyFix(myProject, desc)
                val manager = PsiDocumentManager.getInstance(myProject)
                val document = manager.getDocument(PsiManager.getInstance(myProject).findFile(myProjectPom)!!)!!
                manager.commitDocument(document)
                FileDocumentManager.getInstance().saveDocument(document)
            }

            println(myProjectPom.contentsToByteArray().toString(Charsets.UTF_8))
        }, "quick-fix-$name", "Kotlin")
    }

    private fun mkJavaFile() {
        val sourceFolder = getContentRoots("configure-maven-test").single().getSourceFolders(JavaSourceRootType.SOURCE).single()
        ApplicationManager.getApplication().runWriteAction {
            val javaFile = sourceFolder.file?.toPsiDirectory(myProject)?.createFile("Test.java") ?: throw IllegalStateException()
            javaFile.virtualFile.setBinaryContent("class Test {}\n".toByteArray())
        }


        println(sourceFolder)
    }

    private inline fun <reified T : LocalInspectionTool> runInspection(): List<Pair<SimplifiedProblemDescription, ProblemDescriptorBase>> {
        val inspectionClass = T::class.java

        val toolWrapper = LocalInspectionToolWrapper(inspectionClass.newInstance() as LocalInspectionTool)

        val scope = AnalysisScope(myProject)
        val inspectionManager = (InspectionManager.getInstance(myProject) as InspectionManagerEx)
        val globalContext = CodeInsightTestFixtureImpl.createGlobalContextForTool(scope, myProject, inspectionManager, toolWrapper)

        InspectionTestUtil.runTool(toolWrapper, scope, globalContext)
        val presentation = globalContext.getPresentation(toolWrapper)

        return presentation.problemElements.filter { it.key.name == "pom.xml" }
                .values
                .flatMap { it.toList() }
                .mapNotNull { it as? ProblemDescriptorBase }
                .map { SimplifiedProblemDescription(it.descriptionTemplate, it.psiElement.text, it.fixes?.map { it.name } ?: emptyList()) to it }
    }

    private inline fun <reified T : LocalInspectionTool> runInspectionAndAssert(expected: List<SimplifiedProblemDescription>, block: (List<ProblemDescriptorBase>) -> Unit) {
        val problems = runInspection<T>()
        assertEquals(expected.sortedBy { it.text }, problems.map { it.first }.sortedBy { it.text })
        block(problems.map { it.second }.sortedBy { it.descriptionTemplate })
    }

    private data class SimplifiedProblemDescription(val text: String, val elementText: String, val fixNames: List<String>)
}