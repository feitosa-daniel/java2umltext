package com.java2umltext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.PropertiesDefaultProvider;
import picocli.CommandLine.Unmatched;

import com.java2umltext.export.Format;
import com.java2umltext.model.Document;
import com.java2umltext.model.UML;
import com.java2umltext.model.Visibility;


public class Client {
    @Command(mixinStandardHelpOptions = true)
    static class DefaultConfig {
        @Option(names = { "-c", "--config" }, description = "Set options via a config file.")
        File configPath = new File(System.getProperty("user.dir"), "java2umltext.properties");

        @Unmatched
        List<String> remainder;
    }

    @Command(name = "java2umltext", mixinStandardHelpOptions = true, version = "java2umltext 0.1",
            description = "Create UML Class Diagrams in text formats from Java source")
    static class Config implements Runnable {
        @Option(names = { "-c", "--config" }, description = "Set options via a config file.")
        File configPath = new File(System.getProperty("user.dir"), "java2umltext.properties");

        @Option(names = { "-o" }, description = "Output file path", descriptionKey = "output")
        Optional<File> outFilePath;

        @Option(names = { "-l", "--language-level" }, defaultValue = "JAVA_8", description = "Source code language level. One of ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}", descriptionKey = "languageLevel")
        ParserConfiguration.LanguageLevel languageLevel;
        
        @Option(names = { "-f", "--field-modifiers" }, defaultValue = "public", description = "Field modifiers. One or more of ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}", descriptionKey = "fieldModifiers")
        List<Visibility> fieldModifiers;
        
        @Option(names = { "-m", "--method-modifiers" }, defaultValue = "public", description = "Method modifiers. One or more of ${COMPLETION-CANDIDATES}. Default: ${DEFAULT-VALUE}", descriptionKey = "methodModifiers")
        List<Visibility> methodModifiers;
        
        @Option(names = "--package", negatable = true, defaultValue = "false", description = "Show package name. Default: ${DEFAULT-VALUE}", descriptionKey = "package")
        boolean showPackage;
        
        @Option(names = "--constructors", negatable = true, defaultValue = "true", description = "Show constructors. Default: ${DEFAULT-VALUE}", descriptionKey = "constructors")
        boolean showConstructors;

        @Option(names = "--field-relationships", negatable = true, defaultValue = "true", description = "Show relationships found based on field types. Default: ${DEFAULT-VALUE}", descriptionKey = "fieldRelationships")
        boolean showFieldRelationships;
        
        @Option(names = "--method-relationships", negatable = true, defaultValue = "true", description = "Show relationships found based on method return and parameter types. Default: ${DEFAULT-VALUE}", descriptionKey = "methodRelationships")
        boolean showMethodRelationships;
        
        @Parameters(paramLabel = "FORMAT", description = "Export format. One of ${COMPLETION-CANDIDATES}")
        Format format;

        @Parameters(paramLabel = "SOURCES", description = "Path to Java file or directory")
        List<Path> sources;
        
        @Override
        public void run() {
            Document doc = format.newDocument();
            StaticJavaParser.getConfiguration().setLanguageLevel(languageLevel);
            
            // Extract diagram elements
            try {
                for (Path source : multiWalk(sources, List.of(".java"))) {
                    CompilationUnit cu = StaticJavaParser.parse(source.toFile());
                    Optional<PackageDeclaration> pd = cu.getPackageDeclaration();
                    VoidVisitor<UML> v = new ASTVisitor(pd.isPresent() ? pd.get().getNameAsString() : "", this);
                    v.visit(cu, doc);
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            doc.removeForeignRelations();

            // Export diagram
            if (!outFilePath.isPresent()) {
                System.out.println(doc.export());
            } else {
                if (outFilePath.get().isDirectory()) {
                    System.out.println("Error: output path is a directory.");
                    System.exit(1);
                }else if (!outFilePath.get().getParentFile().exists()) {
                    outFilePath.get().getParentFile().mkdirs();
                }

                try {
                    Files.writeString(outFilePath.get().toPath(), doc.export(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }

        private static Set<Path> multiWalk(Collection<Path> paths, Collection<String> extensions) {
            return paths.stream()
                .flatMap(p -> {
                    try { return Files.walk(p); } 
                    catch (IOException e) { return Stream.empty(); }
                })
                .filter(p -> extensions.stream().anyMatch(e -> p.toString().endsWith(e)))
                .map(p -> {
                    try { return p.toRealPath(); }
                    catch (IOException e) { return p; }
                })
                .collect(Collectors.toSet());
        }
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("picocli.trace", "OFF");
        DefaultConfig defaults = new DefaultConfig();
        CommandLine cmd = new CommandLine(defaults);
        cmd.parseArgs(args);
        if (cmd.isUsageHelpRequested()) {
            new CommandLine(new Config()).usage(System.out);
        } else if (cmd.isVersionHelpRequested()) {
            new CommandLine(new Config()).printVersionHelp(System.out);
        } else {
            cmd = new CommandLine(new Config());
            cmd.setCaseInsensitiveEnumValuesAllowed(true);
            int exitCode = cmd
                .setDefaultValueProvider(new PropertiesDefaultProvider(defaults.configPath))
                .execute(args);
            System.exit(exitCode);
        }
    }
}
