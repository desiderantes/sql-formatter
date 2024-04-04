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

import org.jooq.conf.TransformUnneededArithmeticExpressions
import org.jooq.conf.Transformation

data class LintOptions(
    val transformInlineBindValuesForFieldComparisons: Boolean? = false,
    val transformAnsiJoinToTableLists: Boolean = false,
    val transformInConditionSubqueryWithLimitToDerivedTable: Transformation = Transformation.WHEN_NEEDED,
    val transformQualify: Transformation = Transformation.WHEN_NEEDED,
    val transformTableListsToAnsiJoin: Boolean = false,
    val transformRownum: Transformation = Transformation.NEVER,
    val transformUnneededArithmeticExpressions: TransformUnneededArithmeticExpressions = TransformUnneededArithmeticExpressions.NEVER,
    val transformGroupByColumnIndex: Transformation = Transformation.WHEN_NEEDED,
    val transformInlineCTE: Transformation = Transformation.WHEN_NEEDED,
)
