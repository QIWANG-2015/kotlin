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

package org.jetbrains.kotlin.idea.intentions.branchedTransformations.intentions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.idea.intentions.SelfTargetingRangeIntention
import org.jetbrains.kotlin.idea.intentions.branchedTransformations.getSubjectToIntroduce
import org.jetbrains.kotlin.idea.intentions.branchedTransformations.introduceSubject
import org.jetbrains.kotlin.idea.util.CommentSaver
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.getNextSiblingIgnoringWhitespaceAndComments
import java.util.*

class IfToWhenIntention : SelfTargetingRangeIntention<KtIfExpression>(KtIfExpression::class.java, "Replace 'if' with 'when'") {
    override fun applicabilityRange(element: KtIfExpression): TextRange? {
        if (element.then == null) return null
        return element.ifKeyword.textRange
    }

    private fun canPassThrough(expression: KtExpression?): Boolean =
            when (expression) {
                is KtReturnExpression, is KtThrowExpression ->
                    false
                is KtBlockExpression ->
                    expression.statements.all { canPassThrough(it) }
                is KtIfExpression ->
                    canPassThrough(expression.then) || canPassThrough(expression.`else`)
                else ->
                    true
            }

    private fun buildNextBranch(ifExpression: KtIfExpression): KtExpression? {
        var nextSibling = ifExpression.getNextSiblingIgnoringWhitespaceAndComments() ?: return null
        return when (nextSibling) {
            is KtIfExpression ->
                if (nextSibling.then == null) null else nextSibling
            else -> {
                val builder = StringBuilder()
                while (true) {
                    builder.append(nextSibling.text)
                    nextSibling = nextSibling.nextSibling ?: break
                }
                KtPsiFactory(ifExpression).createBlock(builder.toString())
            }
        }
    }

    override fun applyTo(element: KtIfExpression, editor: Editor?) {
        val commentSaver = CommentSaver(element)

        val toDelete = ArrayList<PsiElement>()
        var whenExpression = KtPsiFactory(element).buildExpression {
            appendFixedText("when {\n")

            var ifExpression = element
            var rootIfExpression = ifExpression
            var canPassThrough = false
            while (true) {
                val condition = ifExpression.condition
                val orBranches = ArrayList<KtExpression>()
                if (condition != null) {
                    orBranches.addOrBranches(condition)
                }

                appendExpressions(orBranches, separator = "||")

                appendFixedText("->")

                val thenBranch = ifExpression.then
                appendExpression(thenBranch)
                appendFixedText("\n")

                canPassThrough = canPassThrough || canPassThrough(thenBranch)

                val originalElse = ifExpression.`else`
                val elseBranch = originalElse ?: if (canPassThrough) break else buildNextBranch(rootIfExpression) ?: break
                if (originalElse == null) {
                    var nextSibling = rootIfExpression.nextSibling
                    while (nextSibling != null && nextSibling != elseBranch) {
                        if (nextSibling !is PsiWhiteSpace && nextSibling.node.elementType != KtTokens.RBRACE) {
                            toDelete.add(nextSibling)
                        }
                        nextSibling = nextSibling.nextSibling
                    }
                }
                if (elseBranch is KtIfExpression) {
                    if (originalElse == null) {
                        rootIfExpression = elseBranch
                        toDelete.add(elseBranch)
                    }
                    ifExpression = elseBranch
                }
                else {
                    appendFixedText("else->")
                    appendExpression(elseBranch)
                    appendFixedText("\n")
                    break
                }
            }

            appendFixedText("}")
        } as KtWhenExpression


        if (whenExpression.getSubjectToIntroduce() != null) {
            whenExpression = whenExpression.introduceSubject()
        }

        val result = element.replace(whenExpression)
        commentSaver.restore(result)
        toDelete.forEach {
            it.delete()
        }
    }

    private fun MutableList<KtExpression>.addOrBranches(expression: KtExpression): List<KtExpression> {
        if (expression is KtBinaryExpression && expression.operationToken == KtTokens.OROR) {
            val left = expression.left
            val right = expression.right
            if (left != null && right != null) {
                addOrBranches(left)
                addOrBranches(right)
                return this
            }
        }

        add(KtPsiUtil.safeDeparenthesize(expression))
        return this
    }
}
