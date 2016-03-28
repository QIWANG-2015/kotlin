package org.jetbrains.kotlin.gradle

import org.jetbrains.kotlin.gradle.util.findFileByName
import org.jetbrains.kotlin.gradle.util.getFileByName
import org.jetbrains.kotlin.gradle.util.modify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClassFileIsRemovedIT : BaseGradleIT() {
    @Test
    fun testClassIsRemovedNonIC() {
        doTestClassIsRemoved(defaultBuildOptions())
    }

    @Test
    fun testClassIsRemovedIC() {
        doTestClassIsRemoved(defaultBuildOptions().copy(incremental = true))
    }

    private fun doTestClassIsRemoved(buildOptions: BuildOptions) {
        val project = Project("kotlinProject", "2.10")
        project.build("build", options = buildOptions) {
            assertSuccessful()
        }

        val dummyFile = project.projectDir.getFileByName("Dummy.kt")
        assertTrue(dummyFile.delete(), "Could not delete $dummyFile")

        project.build("build", options = buildOptions) {
            assertSuccessful()
            val dummyClassFile = project.projectDir.findFileByName("Dummy.class")
            assertEquals(null, dummyClassFile, "$dummyClassFile should not exist!")
        }
    }

    @Test
    fun testClassIsRenamedNonIC() {
        doTestClassIsRenamed(defaultBuildOptions())
    }

    @Test
    fun testClassIsRenamedIC() {
        doTestClassIsRenamed(defaultBuildOptions().copy(incremental = true))
    }

    fun doTestClassIsRenamed(buildOptions: BuildOptions) {
        val project = Project("kotlinProject", "2.10")
        project.build("build", options = buildOptions) {
            assertSuccessful()
        }

        val dummyFile = project.projectDir.getFileByName("Dummy.kt")
        dummyFile.modify { it.replace("Dummy", "ForDummies") }

        project.build("build", options = buildOptions) {
            assertSuccessful()
            val dummyClassFile = project.projectDir.findFileByName("Dummy.class")
            assertNull(dummyClassFile, "$dummyClassFile should not exist!")

            val forDummiesClassFile = project.projectDir.findFileByName("ForDummies.class")
            assertNotNull(forDummiesClassFile, "$forDummiesClassFile should exist!")
        }
    }
}