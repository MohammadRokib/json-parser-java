<a id="top"></a>

<div align="center">

# json-parser - A Java JSON Parser & Validator

[![Java](https://img.shields.io/badge/Java-24+-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

`json-parser` is a from-scratch JSON lexer and recursive-descent parser written entirely in Java, with no external runtime dependencies. It validates JSON documents for well-formedness against the [JSON grammar](https://www.json.org/json-en.html) and reports the result through a Unix-style exit code.

This project was built as part of the [Build Your Own JSON Parser Challenge](https://codingchallenges.fyi/challenges/challenge-json-parser).

</div>

<p align="center">
  <a href="#features">Explore the docs</a> ·
  <a href="https://github.com/MohammadRokib/json-parser-java/issues">Report Bug</a> ·
  <a href="https://www.linkedin.com/in/m0hammadrokib/">LinkedIn</a> ·
  <a href="mailto:mohammadrokibkhan@gmail.com">Email</a>
</p>

---

## Features

- **Full JSON grammar coverage**: objects, arrays, strings (with escape-sequence and `\uXXXX` unicode validation), numbers (integers, decimals, exponents), and the `true` / `false` / `null` keywords.
- **Line/column-accurate error reporting**: every `JsonParseException` carries the exact line and column of the offending token, not just "somewhere in the file."
- **Nesting depth guard**: both objects and arrays protect their own recursion against a configurable `MAX_DEPTH`, so pathological input fails with a clean `JsonParseException` instead of an uncaught `StackOverflowError`.
- **Unix-style CLI**: exits `0` for valid JSON, `1` for invalid JSON or I/O errors, with a matching message on `stdout`/`stderr`.
- **Verified against the reference suite**: passes the full official [json.org `JSON_checker`](https://www.json.org/JSON_checker/) test suite (36/36 fixtures) alongside a hand-written JUnit 5 suite.

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Prerequisites

- **Java Development Kit (JDK) 24** or higher.
- **Maven** on your `PATH` (no wrapper is committed to this repo).

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Installation & Building

1. Clone the repository:
   ```bash
   git clone https://github.com/MohammadRokib/json-parser-java.git
   cd json-parser-java
   ```

2. Compile, test, and package with Maven:
   ```bash
   mvn clean package
   ```
   This runs the full test suite and produces `target/json-parser-1.0-SNAPSHOT.jar`.

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Usage

The jar has no `Main-Class` configured in its manifest yet, so it's run by pointing `java` at the jar with `-cp` and the fully-qualified entry class.

### Syntax
```bash
java -cp target/json-parser-1.0-SNAPSHOT.jar org.example.jsonparser.JsonParser <file.json>
```

### Examples

**1. Valid JSON:**
```bash
$ java -cp target/json-parser-1.0-SNAPSHOT.jar org.example.jsonparser.JsonParser valid.json
Valid JSON
$ echo $?
0
```

**2. Invalid JSON, with a precise error location:**
```bash
$ java -cp target/json-parser-1.0-SNAPSHOT.jar org.example.jsonparser.JsonParser invalid.json
Invalid JSON: Expected a value but found RBRACE at line 1 column 7
$ echo $?
1
```

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Architecture & Design

Parsing is split into the classic two-stage pipeline: lexical analysis, then syntactic analysis.

```
source text → Reader → Lexer.nextToken() → Token(TokenType, lexeme, line, column) → Parser → valid/invalid → JsonParser (CLI)
```

1. **`Lexer.java`**: streams the input one character at a time through a plain `java.io.Reader` (no pushback/lookahead buffering needed - one character of internal state is enough) and turns it into a stream of `Token`s: braces, brackets, colon, comma, strings, numbers, and keywords. Tracks `line`/`column` as it advances so every downstream error can point at an exact location.
2. **`Token.java`** / **`TokenType.java`**: an immutable `record Token(TokenType type, String lexeme, int line, int column)` and a flat enum of the token kinds plus `EOF`.
3. **`Parser.java`**: a recursive-descent parser (`parseObject`, `parseArray`, `parsePairs`, `parseValue`) that walks the token stream and validates it against the JSON grammar. Both `parseObject` and `parseArray` guard their own entry against `MAX_DEPTH`, so deeply nested input of either kind fails cleanly instead of exhausting the JVM stack.
4. **`JsonParseException.java`**: an unchecked exception that always carries `line`/`column`, so every failure - from the lexer or the parser - reports exactly where it happened.
5. **`JsonParser.java`**: the CLI entry point. Reads a file path argument, wires `Lexer` → `Parser`, and translates the outcome into `Valid JSON` / `Invalid JSON: <reason>` plus the matching exit code.

**Design note:** this parser validates *syntax* - it does not build a JSON value tree (no `JsonObject`/`JsonArray`/etc. is ever constructed). That matches the actual contract of the [Coding Challenges JSON Parser](https://codingchallenges.fyi/challenges/challenge-json-parser) spec, whose only observable requirement at every step is "report valid or invalid, with the right exit code" - not "return usable parsed data."

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Testing

```bash
mvn test
```

The suite (90 tests, JUnit 5) is split across:
- **`LexerTest`, `LexerStringTest`, `LexerNumberTest`, `LexerKeywordTest`**: token-level unit tests, largely `@ParameterizedTest`-driven.
- **`ParserTest`**: grammar-level tests for valid and invalid JSON snippets, including nested objects/arrays.
- **`OfficialSuiteTest`**: runs every fixture in `src/test/resources/json_checker` (the official json.org `pass*.json` / `fail*.json` suite) and asserts the parser accepts/rejects each one correctly - the strongest correctness signal in the repo, since it's an independent external reference suite rather than a test the author wrote for their own implementation.

<p align="right"><a href="#top">Back to top ⬆️</a></p>

---

## Contact

Mohammad Rokib

- **[LinkedIn](https://www.linkedin.com/in/m0hammadrokib/)**
- **[Email](mailto:mohammadrokibkhan@gmail.com)**
- **[GitHub](https://github.com/MohammadRokib)**
- **[Project Link: json-parser-java](https://github.com/MohammadRokib/json-parser-java)**

<p align="right"><a href="#top">Back to top ⬆️</a></p>
