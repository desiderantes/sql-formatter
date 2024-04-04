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

import org.jooq.conf.*
import org.jooq.impl.DSL
import java.io.File
import java.util.*
import java.util.stream.Collectors

class Formatter (private val formatOptions: FormatOptions, private val lintOptions: LintOptions = LintOptions()) {

    private val errors: MutableList<ErrorData> = arrayListOf()

    val errorData: List<ErrorData>
        get() = Collections.unmodifiableList(errors)

    private val ctx = DSL.using(
        formatOptions.dialect, Settings()
            //Parser settings
            .withParseDialect(formatOptions.dialect)
            .withParseUnknownFunctions(ParseUnknownFunctions.IGNORE)
            .withParseSetCommands(true)
            .withParseUnsupportedSyntax(ParseUnsupportedSyntax.IGNORE)
            .withParseIgnoreComments(false)
            //Render settings
            .withRenderTable(formatOptions.writeTable)
            .withRenderFormatted(true)
            .withRenderNameCase(formatOptions.nameCasing)
            .withRenderQuotedNames(formatOptions.nameQuoting)
            .withRenderKeywordCase(formatOptions.keywordCasing)
            .withRenderImplicitJoinType(RenderImplicitJoinType.DEFAULT)
            // Transform settings
            .withTransformInlineCTE(lintOptions.transformInlineCTE)
            .withTransformAnsiJoinToTableLists(lintOptions.transformAnsiJoinToTableLists)
            .withTransformInConditionSubqueryWithLimitToDerivedTable(lintOptions.transformInConditionSubqueryWithLimitToDerivedTable)
            .withTransformQualify(lintOptions.transformQualify)
            .withTransformTableListsToAnsiJoin(lintOptions.transformTableListsToAnsiJoin)
            .withTransformRownum(lintOptions.transformRownum)
            .withTransformUnneededArithmeticExpressions(lintOptions.transformUnneededArithmeticExpressions)
            .withTransformGroupByColumnIndex(lintOptions.transformGroupByColumnIndex)
            .withTransformUnneededArithmeticExpressions(lintOptions.transformUnneededArithmeticExpressions)

            .withRenderFormatting(
                RenderFormatting()
                    .withIndentation(formatOptions.indentType.indentationValue().repeat(formatOptions.indentSize))
                    .withNewline(formatOptions.lineEnding)
                    .withPrintMargin(formatOptions.maxLineLength)
            )
    )

    private val parser = ctx.parser()

    fun format(file: File) {
        val input = file.inputStream()
        val inputStr = input.bufferedReader().use { it.readText() }
        val output = file.printWriter(formatOptions.encoding)
        try {
            val formatted = parser.parse(inputStr).queryStream().map { ctx.render(it) }.collect(Collectors.joining(formatOptions.lineEnding))
            output.write(formatted)
        } catch (e: Exception) {
            errors.add(ErrorData(file.name, e.message ?: "Unknown error", e.stackTrace.joinToString("\n")))
            output.write(inputStr)
        } finally {
            input.close()
            output.close()
        }
    }

    fun format(input: String) : String {
        return try {
            parser.parse(input).queryStream().map { ctx.render(it) }.collect(Collectors.joining(formatOptions.lineEnding))
        } catch (e: Exception) {
            errors.add(ErrorData("stdin", e.message ?: "Unknown error", e.stackTrace.joinToString("\n")))
            input
        }

    }
}

data class ErrorData(val filename: String, val error: String, val stacktrace: String)