/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.idea.highlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtVisitorVoid

internal class SoftKeywordsHighlightingVisitor(private val holder: AnnotationHolder) : KtVisitorVoid() {

    override fun visitElement(element: PsiElement) {
        if (element is LeafPsiElement) {
            val elementType = element.elementType
            if (KtTokens.SOFT_KEYWORDS.contains(elementType)) {
                var attributes = KotlinHighlightingColors.KEYWORD
                if (KtTokens.MODIFIER_KEYWORDS.contains(elementType)) {
                    attributes = KotlinHighlightingColors.BUILTIN_ANNOTATION
                }
                holder.createInfoAnnotation(element as PsiElement, null).textAttributes = attributes
            }
            if (KtTokens.SAFE_ACCESS == elementType) {
                holder.createInfoAnnotation(element as PsiElement, null).textAttributes = KotlinHighlightingColors.SAFE_ACCESS
            }
        }
    }

    override fun visitLambdaExpression(lambdaExpression: KtLambdaExpression) {
        if (ApplicationManager.getApplication().isUnitTestMode) return
        val functionLiteral = lambdaExpression.functionLiteral
        holder.createInfoAnnotation(functionLiteral.lBrace, null).textAttributes = KotlinHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW
        val closingBrace = functionLiteral.rBrace
        if (closingBrace != null) {
            holder.createInfoAnnotation(closingBrace, null).textAttributes = KotlinHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW
        }
        val arrow = functionLiteral.arrow
        if (arrow != null) {
            holder.createInfoAnnotation(arrow, null).textAttributes = KotlinHighlightingColors.FUNCTION_LITERAL_BRACES_AND_ARROW
        }
    }
}
