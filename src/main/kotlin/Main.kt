/**
 * Copyright 2024 Mario Daniel Ruiz Saavedra (desiderantes93@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.desiderantes.sqlformatter

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.mutuallyExclusiveOptions
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import java.io.File

sealed interface InputType {
    data class FileInput(val files: List<File>) : InputType
    data class StringInput(val string: String) : InputType
}

class Format : CliktCommand () {
    private val input by mutuallyExclusiveOptions(
        option("--files", help = "Files or directories to format" ).file(mustExist = true, canBeFile = true, canBeDir = true).transformAll { InputType.FileInput(it) },
        option("--string", help = "String to format").convert { InputType.StringInput(it) }
    ).required()
    private val configOptions: Pair<FormatOptions, LintOptions> by option(help = "Formatting options").file().convert{
        val parsedConf = ConfigFactory.parseFile(it)
        return@convert parsedConf.extract<FormatOptions>("formattingOptions", FormatOptions()) to parsedConf.extract<LintOptions>("lintOptions", LintOptions())
    }.default(FormatOptions() to LintOptions())



    override fun run() {
        val formatter = Formatter(configOptions.first, configOptions.second)
        when (input) {
            is InputType.FileInput -> {
                (input as InputType.FileInput).files.forEach { file ->
                    if (file.isDirectory) {
                        file.walkTopDown().filter { it.isFile }.forEach {f ->
                            formatter.format(f)
                        }
                        return@forEach
                    }
                    else {
                        formatter.format(file)
                    }
                }
            }
            is InputType.StringInput -> {
                echo(formatter.format((input as InputType.StringInput).string))
            }
        }
        if (formatter.errorData.isNotEmpty()) {
            formatter.errorData.forEach {
                echo("Error formatting ${it.filename}: ${it.error}", err = true)
            }
        }
    }
}

fun main(args: Array<String>) {
    Format().main(args)
}


