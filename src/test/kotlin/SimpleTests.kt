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

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File


class SimpleTests {
    private var formatter: Formatter = Formatter(FormatOptions())


    @BeforeEach
    fun init() {
        formatter = Formatter(FormatOptions())
        System.setProperty("org.jooq.no-tips", "true")
        System.setProperty("org.jooq.no-logo", "true")
    }

    @Test
    fun testItWorks() {
        val result = formatter.format(
            //language=SQL
            """
            SELECT
  "member_id" AS "member_id__0",
  COUNT(DISTINCT "order_id") AS "order_id__cnt_distinct__0"
FROM "orders"
GROUP BY "member_id__0"
LIMIT 20000;
        """.trimIndent())
        println(result)
        println(formatter.errorData)
        assertTrue(formatter.errorData.isEmpty())
    }

    @Test
    fun testParseConf() {
        val conf = ConfigFactory.parseURL(this::class.java.getResource("/options.conf")!!).extract<FormatOptions>("formattingOptions")
        println(conf)
    }

    @Test
    fun testFormatFileFails() {
        val file = File(this::class.java.getResource("/mysql-test.sql")!!.file)
        formatter.format(file)
        println(formatter.errorData)
        assertTrue(formatter.errorData.isNotEmpty())
    }
}