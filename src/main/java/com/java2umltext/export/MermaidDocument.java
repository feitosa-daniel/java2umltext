package com.java2umltext.export;

import java.util.stream.Collectors;

import com.java2umltext.model.ClassWrapper;
import com.java2umltext.model.Document;
import com.java2umltext.model.Relationship;

public class MermaidDocument extends Document {

    @Override
    protected String getHeader() {
        return "classDiagram\n";
    }

    @Override
    protected String getFooter() {
        return "";
    }

    @Override
    protected String exportClass(ClassWrapper cw) {
        String fullname = ((cw.pkg() == null || cw.pkg().isBlank()) ? "" : (cw.pkg().replace(".", "_") + "_")) + cw.name().replace(".", "_");

        String str = "class " + fullname + " { ";
        
        if (cw.type().contains("abstract")) {
            str += "\n<<abstract>>";
        }
        if (cw.type().contains("interface")) {
            str += "\n<<interface>>";
        }
        if (cw.type().contains("enum")) {
            str += "\n<<enum>>";
        }
        if (cw.type().contains("record")) {
            str += "\n<<record>>";
        }

        
        str += cw.fields().isEmpty() ? "" : "\n" +
            cw.fields().stream()
            .map(f -> f.visibility().symbol() + " " 
                +(f.type().isBlank() ? "" : f.type().replaceAll("[<>]", "~") + " ")
                + f.name()
                + (f.isStatic() ? "$" : ""))
            .collect(Collectors.joining("\n"));
        
        str += cw.methods().isEmpty() ? "" : "\n" +
            cw.methods().stream()
            .map(m -> m.visibility().symbol() + " " 
                + m.returnType().replaceAll("[<>]", "~") + " "
                + m.name()
                + "(" + m.parameters().stream().map(p -> p.replaceAll("[<>]", "~")).collect(Collectors.joining(",")) + ")"
                + (m.isAbstract() ? "*" : "")
                + (m.isStatic() ? "$" : ""))
            .collect(Collectors.joining("\n"));

        str += "\n}\n";
        
        return str;
    }

    @Override
    protected String exportRelationship(Relationship r) {
        return r.source().replace(".", "_") 
            + " " 
            + (r.type().equals("+..") ? "<.." : r.type())
            + " " 
            + r.target().replace(".", "_")
            + (r.type().equals("+..") ? " : << contains >>" : "");
    }
}
