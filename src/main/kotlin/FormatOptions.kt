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

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.github.config4k.ClassContainer
import io.github.config4k.CustomType
import io.github.config4k.registerCustomType
import org.jooq.SQLDialect
import org.jooq.conf.RenderKeywordCase
import org.jooq.conf.RenderNameCase
import org.jooq.conf.RenderQuotedNames
import org.jooq.conf.RenderTable
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

data class FormatOptions(
    val dialect: SQLDialect = SQLDialect.POSTGRES,
    val indentSize: Int = 2,
    val indentType: IndentType = IndentType.SPACE,
    val maxLineLength: Int = 120,
    val writeTable: RenderTable = RenderTable.WHEN_MULTIPLE_TABLES,
    val nameQuoting: RenderQuotedNames = RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED,
    val nameCasing: RenderNameCase = RenderNameCase.AS_IS,
    val keywordCasing: RenderKeywordCase = RenderKeywordCase.AS_IS,
    val lineEnding: String = "\n",
    val encoding: Charset = StandardCharsets.UTF_8
) {

    enum class IndentType {
        SPACE,
        TAB;
        fun indentationValue(): String {
            return when (this) {
                SPACE -> " "
                TAB -> "\t"
            }
        }
    }
    companion object {
        init {
            registerCustomType(object : CustomType {
                override fun parse(clazz: ClassContainer, config: Config, name: String): Any? {
                    return Charset.forName(config.getString(name))
                }

                override fun testParse(clazz: ClassContainer): Boolean {
                    return clazz.mapperClass == Charset::class
                }

                override fun testToConfig(obj: Any): Boolean {
                    return obj is Charset
                }

                override fun toConfig(obj: Any, name: String): Config {
                    return ConfigFactory.empty().withValue(name, ConfigValueFactory.fromAnyRef((obj as Charset).name()))
                }

            })
        }
    }
}