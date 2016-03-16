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

package org.jetbrains.kotlin.idea.intentions.loopToCallChain

import org.jetbrains.kotlin.idea.util.CommentSaver
import org.jetbrains.kotlin.psi.KtCallableDeclaration
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtForExpression

interface ChainedCallGenerator {
    fun generate(pattern: String, vararg args: Any): KtExpression
}

interface Transformation {
    val inputVariable: KtCallableDeclaration
    val expressionsInLambdas: Collection<KtExpression> //TODO: or should be KtLambdaExpression? //TODO: do we need it at all?
}

interface SequenceTransformation : Transformation {
    val affectsIndex: Boolean

    fun generateCode(chainedCallGenerator: ChainedCallGenerator): KtExpression
}

interface ResultTransformation : Transformation {
    val canIncludeFilter: Boolean
    val canIncludeMap: Boolean

    fun createCommentSaver(loop: KtForExpression): CommentSaver

    fun convertLoop(loop: KtForExpression, params: TransformLoopParams): KtExpression
}

data class FilterOrMap(val expression: KtExpression, val workingVariable: KtCallableDeclaration)

class TransformLoopParams(
        val chainedCallGenerator: ChainedCallGenerator,
        val filter: FilterOrMap?,
        val map: FilterOrMap?,
        val commentSaver: CommentSaver
)

data class MatchingState(
        val statements: Collection<KtExpression>,
        val workingVariable: KtCallableDeclaration,
        val indexVariable: KtCallableDeclaration?,
        val loop: KtForExpression
)

interface SequenceTransformationMatcher {
    fun match(state: MatchingState): SequenceTransformationMatch?
}

class SequenceTransformationMatch(
        val transformations: Collection<SequenceTransformation>,
        val newState: MatchingState
) {
    init {
        assert(transformations.isNotEmpty())
    }

    constructor(transformation: SequenceTransformation, newState: MatchingState) : this(listOf(transformation), newState)
}

interface ResultTransformationMatcher {
    fun match(state: MatchingState): ResultTransformationMatch?
}

class ResultTransformationMatch(
        val resultTransformation: ResultTransformation,
        val sequenceTransformations: Collection<SequenceTransformation> = listOf()
)
