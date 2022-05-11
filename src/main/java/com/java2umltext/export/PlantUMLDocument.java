package com.java2umltext.export;

import java.util.stream.Collectors;

import com.java2umltext.model.ClassWrapper;
import com.java2umltext.model.Document;
import com.java2umltext.model.Relationship;

public class PlantUMLDocument extends Document {

    @Override
    protected String getHeader() {
        return "@startuml\n";
    }

    @Override
    protected String getFooter() {
        return "@enduml";
    }

    @Override
    protected String exportClass(ClassWrapper cw) {
        String fullname = ((cw.pkg() == null || cw.pkg().trim().equals("")) ? "" : (cw.pkg() + ".")) + cw.name();

        String str = (cw.type() == "record" ? "class" : cw.type()) + " "
            + fullname 
            + (cw.type() == "record" ? " <<record>>" : "")
            + " {";
        
        str += cw.fields().isEmpty() ? "" : "\n" +
            cw.fields().stream()
            .map(f ->
                f.visibility().symbol() + " " 
                + (f.isStatic() ? "{static} " : "") 
                + (f.type().isBlank() ? "" : f.type() + " ")
                + f.name())
            .collect(Collectors.joining("\n"));
        
        str += cw.methods().isEmpty() ? "" : "\n" +
            cw.methods().stream()
            .map(m -> 
                m.visibility().symbol() + " " 
                + (m.isStatic() ? "{static} " : "") 
                + (m.isAbstract() ? "{abstract} " : "")
                + m.returnType() + " "
                + m.name()
                + "(" + m.parameters().stream().collect(Collectors.joining(",")) + ")")
            .collect(Collectors.joining("\n"));

        str += "\n}\n";
        
        return str;
    }

    @Override
    protected String exportRelationship(Relationship r) {
        return r.source() + " " + r.type() + " " + r.target();
    }
}
