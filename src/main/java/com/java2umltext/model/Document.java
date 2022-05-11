package com.java2umltext.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Document implements UML {

    protected List<ClassWrapper> classList = new ArrayList<>();
    protected List<Relationship> relationshipList = new ArrayList<>();

    public ClassWrapper addClass(String pkg, String type, String name) {
        ClassWrapper c = new ClassWrapper(this, pkg, type, name);
        classList.add(c);
        return c;
    }

    public void addRelationship(Relationship r) {
        if (relationshipList.stream().noneMatch(i -> r.source().equals(i.source()) && r.target().equals(i.target()))){
            relationshipList.add(r);
        }
    }

    /**
     * Remove relationships involving classes that are not declared in the Document.
     */
    public void removeForeignRelations(){
        relationshipList.removeIf(r-> 
            classList.stream()
                .noneMatch(c -> r.source().equals(c.name())) ||
            classList.stream()
                .noneMatch(c -> r.target().equals(c.name()))
        );
    }
    
    public String export() {
        return getHeader() 
        + classList.stream()
            .map(c -> exportClass(c))
            .collect(Collectors.joining("\n"))
        + "\n"
        + relationshipList.stream()
            .map(r -> exportRelationship(r))
            .collect(Collectors.joining("\n"))
        + "\n"
        + getFooter();
    }

    protected abstract String getHeader();
    protected abstract String getFooter();
    protected abstract String exportClass(ClassWrapper cw);
    protected abstract String exportRelationship(Relationship r);
}
