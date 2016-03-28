package org.jetbrains.kotlin.gradle.util

import java.io.File

fun File.getFileByName(name: String): File =
        findFileByName(name) ?: throw AssertionError("Could not find file with name '$name' in $this")

fun File.findFileByName(name: String): File? =
        walk().filter { it.isFile && it.name.equals(name, ignoreCase = true) }.firstOrNull()

fun File.modify(transform: (String)->String) {
    writeText(transform(readText()))
}