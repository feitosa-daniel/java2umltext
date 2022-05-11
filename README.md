# java2umltext

Create [PlantUML](https://plantuml.com/class-diagram) and [Mermaid](https://mermaid-js.github.io/mermaid/#/classDiagram) class diagrams from Java source code with ease.

- `java2umltext` exports text files (not images).
- `java2umltext` requires JRE 16 or later.
- `java2umltext` can parse source from Java 1.0 (see option `--language`).


## What it is (and is not)

`java2umltext` is a standalone and simple but configurable tool to create class diagrams.

It was developed with education/classroom in mind.

- `java2umltext` **is not** a plugin to be integrated with build tools (for that, see [UMLDoclet](https://github.com/talsma-ict/umldoclet))
- `java2umltext` **does not** cover elaborated use-cases out of the box; e.g., it cannot generate multiple diagrams for each package (again, see [UMLDoclet](https://github.com/talsma-ict/umldoclet)).

I use it in combination with a file watcher and the PlatnUML Visual Studio Code extension to generate PlantUML diagrams I code in the classroom.

## Usage

```
> java -jar path/to/java2umltext.jar <options> FORMAT SOURCES...

FORMAT       Export format (PLANTUML or MERMAID)
SOURCES      Path to one or more Java files or directories
```

| Option                            | Description                   |
|-----------------------------------|-------------------------------|
|`-o=<filepath>`                    | Path to output file (printed to stdout otherwise)|
|`--[no-]package`                   | Show package name. (**default: false**)|
|`--[no-]constructors`              | Show constructors. (**default: true**)|
|`--[no-]field-relationships`       | Show relationships found based on field types. (**default: true**)|
|`--[no-]method-relationships`      | Show relationships found based on method return and parameter types. (**default: true**)|
|`-f, --field-modifiers=<modifier>` | Field modifiers. One or more of PUBLIC, PRIVATE, PROTECTED, DEFAULT. (**default: public**)|
|`-m, --method-modifiers=<modifer>` | Method modifiers. One or more of PUBLIC, PRIVATE, PROTECTED, DEFAULT. (**default: public**)|
|`-l, --language-level=<level>`     | Source code language level. One of JAVA_1_0, JAVA_1_1, JAVA_1_2, JAVA_1_3, JAVA_1_4, JAVA_5, JAVA_6, JAVA_7, JAVA_8, JAVA_9, JAVA_10, JAVA_10_PREVIEW, JAVA_11, JAVA_11_PREVIEW, JAVA_12, JAVA_12_PREVIEW, JAVA_13, JAVA_13_PREVIEW, JAVA_14, JAVA_14_PREVIEW, JAVA_15, JAVA_15_PREVIEW, JAVA_16, JAVA_16_PREVIEW, JAVA_17, JAVA_17_PREVIEW. (**default: JAVA_8**)
|`-c, --config=<filepath>`          | Set options via a config file (format: Java property file).|


### Examples

- Do not include constructors.
  ```
  > java -jar java2umltext.jar --no-constructors ./src/main/java
  ```
- Include private AND public methods (notice you must include multiple `-m`).
  ```
  > java -jar java2umltext.jar -m=public -m=private  ./src/main/java
  ```
- Show package name and include private fields only.
  ```
  > java -jar java2umltext.jar -f=private --package ./src/main/java
  ```

## Build from source

```
> gradlew jar
```

The jar includes the necessary dependencies.

## Credits

This project uses (and packages):
- [javaparser](https://github.com/javaparser/javaparser) for source code data extraction.
- [picocli](https://github.com/remkop/picocli) for argument parsing.
