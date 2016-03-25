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

package org.jetbrains.kotlin.idea.inspections

import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtVisitorVoid
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode

class ArrayInDataClassInspection : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean, session: LocalInspectionToolSession): PsiElementVisitor {
        return object : KtVisitorVoid() {
            override fun visitClass(klass: KtClass) {
                if (!klass.isData()) return
                val constructor = klass.getPrimaryConstructor() ?: return
                val context = constructor.analyze(BodyResolveMode.PARTIAL)
                for (parameter in constructor.valueParameters) {
                    if (!parameter.hasValOrVar()) continue
                    val type = context.get(BindingContext.TYPE, parameter.typeReference) ?: return
                    if (KotlinBuiltIns.isArray(type) || KotlinBuiltIns.isPrimitiveArray(type)) {
                        holder.registerProblem(parameter,
                                               "Array property in data class: it's recommended to override equals() / hashCode()",
                                               ProblemHighlightType.WEAK_WARNING)
                    }
                }
            }
        }
    }
}
