# SQL Formatter

> use jOOQ to format your code!
> (Inspired by the [jOOQ Translate Page](https://www.jooq.org/translate/))


This formatter is very different from the others you see out there. It uses the jOOQ parser to parse your SQL code and
then re-render it in a consistent way. This means that your SQL queries are enforced to be valid, or it leaves them
alone, and it WILL alter your queries (it's a very interesting linter).

I mostly use it as a pre-commit hook for some projects, but you can use it as a CLI tool or in CI/CD as well.

It uses Java 17 because we're in 2024 and Java 17 is the new Java 8.

Licensed under the [Apache License, Version 2.0.](LICENSE.md)

## Installation

You can install it by building it with gradle:

```bash
$ git clone https://github.com/desiderantes/sql-formatter.git
$ cd sql-formatter
$ ./gradlew build
```

Then you'll find both a .zip and .tar file in the `build/distributions` directory. You can extract it and use the
binaries in the bin directory.
