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

package org.jetbrains.kotlin.idea.intentions.loopToCallChain.sequence

import org.jetbrains.kotlin.idea.intentions.loopToCallChain.*
import org.jetbrains.kotlin.psi.KtBreakExpression
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.psiUtil.blockExpressionsOrSingle

//TODO: smart casts can be lost
class FilterTransformation(
        override val inputVariable: KtCallableDeclaration,
        val condition: KtExpression,
        val isInverse: Boolean
) : SequenceTransformation {

    override val affectsIndex: Boolean
        get() = true

    override val expressionsInLambdas: Collection<KtExpression>
        get() = listOf(condition)

    override fun generateCode(chainedCallGenerator: ChainedCallGenerator): KtExpression {
        val lambda = generateLambda(inputVariable, condition)
        val name = if (isInverse) "filterNot" else "filter"
        return chainedCallGenerator.generate("$0$1:'{}'", name, lambda)
    }

    //TODO: merge subsequent filters
    object Matcher : SequenceTransformationMatcher {
        override fun match(state: MatchingState): SequenceTransformationMatch? {
            if (state.indexVariable != null) return null //TODO?

            val ifStatement = state.statements.firstOrNull() as? KtIfExpression ?: return null
            if (ifStatement.`else` != null) return null
            val condition = ifStatement.condition ?: return null
            val then = ifStatement.then ?: return null

            if (state.statements.size == 1) {
                val transformation = FilterTransformation(state.workingVariable, condition, isInverse = false)
                val newState = state.copy(statements = listOf(then))
                return SequenceTransformationMatch(transformation, newState)
            }
            else {
                //TODO: can be also return if it's the same as the statement right after the loop
                val breakExpression = then.blockExpressionsOrSingle().singleOrNull() as? KtBreakExpression ?: return null
                if (!breakExpression.isBreakOfLoop(state.loop)) return null
                val transformation = FilterTransformation(state.workingVariable, condition, isInverse = true)
                val newState = state.copy(statements = state.statements.drop(1))
                return SequenceTransformationMatch(transformation, newState)
            }
        }
    }
}