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

package org.jetbrains.kotlin.codegen;

import com.google.common.collect.Maps;
import com.intellij.openapi.util.Factory;
import com.intellij.util.containers.ContainerUtil;
import kotlin.Triple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.codegen.state.GenerationState;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.resolve.inline.InlineUtil;
import org.jetbrains.org.objectweb.asm.Type;

import java.util.Map;

public class SamWrapperClasses {
    private final GenerationState state;

    private final Map<Triple<SamType, KtFile, Boolean>, Type> samInterfaceToWrapperClass = Maps.newHashMap();

    public SamWrapperClasses(@NotNull GenerationState state) {
        this.state = state;
    }

    @NotNull
    public Type getSamWrapperClass(
            @NotNull final SamType samType,
            @NotNull final KtFile file,
            @NotNull ExpressionCodegen expressionCodegen
    ) {
        final MemberCodegen<?> parentCodegen = expressionCodegen.getParentCodegen();
        final boolean isInsideInline = InlineUtil.isInlineOrContainingInline(expressionCodegen.getContext().getContextDescriptor());
        return ContainerUtil.getOrCreate(
                samInterfaceToWrapperClass,
                new Triple<SamType, KtFile, Boolean>(
                        samType,
                        file,
                        isInsideInline
                ),
                new Factory<Type>() {
                    @Override
                    public Type create() {
                        return new SamWrapperCodegen(state, samType, parentCodegen, isInsideInline).genWrapper(file);
                    }
                }
        );
    }
}
