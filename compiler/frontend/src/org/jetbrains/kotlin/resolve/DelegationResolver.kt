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

package org.jetbrains.kotlin.resolve

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor.Kind.DELEGATION
import org.jetbrains.kotlin.diagnostics.Errors.MANY_IMPL_MEMBER_NOT_IMPLEMENTED
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtDelegatorByExpressionSpecifier
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.OverridingUtil.OverrideCompatibilityInfo.Result.OVERRIDABLE
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeUtils
import java.util.*

class DelegationResolver<T : CallableMemberDescriptor> private constructor(
        private val classOrObject: KtClassOrObject,
        private val ownerDescriptor: ClassDescriptor,
        private val existingMembers: Collection<CallableDescriptor>,
        private val trace: BindingTrace,
        private val memberExtractor: DelegationResolver.MemberExtractor<T>,
        private val typeResolver: DelegationResolver.TypeResolver
) {

    private fun generateDelegatedMembers(): Collection<T> {
        val delegatedMembers = hashSetOf<T>()
        for (delegationSpecifier in classOrObject.getDelegationSpecifiers()) {
            if (delegationSpecifier !is KtDelegatorByExpressionSpecifier) {
                continue
            }
            val typeReference = delegationSpecifier.typeReference ?: continue
            val delegatedInterfaceType = typeResolver.resolve(typeReference)
            if (delegatedInterfaceType == null || delegatedInterfaceType.isError) {
                continue
            }
            val delegatesForTrait = generateDelegatesForInterface(delegatedMembers, delegatedInterfaceType)
            delegatedMembers.addAll(delegatesForTrait)
        }
        return delegatedMembers
    }

    private fun generateDelegatesForInterface(existingDelegates: Collection<T>, delegatedInterfaceType: KotlinType): Collection<T> =
            generateDelegationCandidates(delegatedInterfaceType).filterTo(hashSetOf<T>()) { candidate ->
                !isOverridingAnyOf(candidate, existingMembers) &&
                !checkClashWithOtherDelegatedMember(existingDelegates, candidate)
            }

    private fun generateDelegationCandidates(delegatedInterfaceType: KotlinType): Collection<T> =
            getOverridableMembers(delegatedInterfaceType).map { memberDescriptor ->
                val newModality = if (memberDescriptor.modality == Modality.ABSTRACT) Modality.OPEN else memberDescriptor.modality
                @Suppress("UNCHECKED_CAST")
                (memberDescriptor.copy(ownerDescriptor, newModality, Visibilities.INHERITED, DELEGATION, false) as T)
            }

    private fun checkClashWithOtherDelegatedMember(delegatedMembers: Collection<T>, candidate: T): Boolean {
        val alreadyDelegatedMember = delegatedMembers.firstOrNull { isOverridableBy(it, candidate) }
        if (alreadyDelegatedMember != null) {
            //trying to delegate to many interfaces with the same methods
            trace.report(MANY_IMPL_MEMBER_NOT_IMPLEMENTED.on(classOrObject, classOrObject, alreadyDelegatedMember))
            return true
        }
        return false
    }

    private fun getOverridableMembers(interfaceType: KotlinType): Collection<T> {
        return memberExtractor.getMembersByType(interfaceType).filter { descriptor -> descriptor.modality.isOverridable }
    }

    interface MemberExtractor<T : CallableMemberDescriptor> {
        fun getMembersByType(type: KotlinType): Collection<T>
    }

    interface TypeResolver {
        fun resolve(reference: KtTypeReference): KotlinType?
    }

    companion object {
        fun <T : CallableMemberDescriptor> generateDelegatedMembers(
                classOrObject: KtClassOrObject,
                ownerDescriptor: ClassDescriptor,
                existingMembers: Collection<CallableDescriptor>,
                trace: BindingTrace,
                memberExtractor: MemberExtractor<T>,
                typeResolver: TypeResolver
        ): Collection<T> =
                DelegationResolver(classOrObject, ownerDescriptor, existingMembers, trace, memberExtractor, typeResolver).generateDelegatedMembers()

        private fun isOverridingAnyOf(
                candidate: CallableMemberDescriptor,
                possiblyOverriddenBy: Collection<CallableDescriptor>
        ): Boolean =
                possiblyOverriddenBy.any { isOverridableBy(it, candidate) }

        private fun isOverridableBy(memberOne: CallableDescriptor, memberTwo: CallableDescriptor): Boolean =
                OverridingUtil.DEFAULT.isOverridableBy(memberOne, memberTwo).result == OVERRIDABLE

        private fun isNotTrait(descriptor: DeclarationDescriptor?): Boolean {
            if (descriptor is ClassDescriptor) {
                val kind = descriptor.kind
                return kind != ClassKind.INTERFACE
            }
            return false
        }
    }
}
