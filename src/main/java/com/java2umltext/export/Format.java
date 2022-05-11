package com.java2umltext.export;

import com.java2umltext.model.Document;

public enum Format {
    PLANTUML,
    MERMAID;

    public Document newDocument() {
        switch(this){
            default:
            case PLANTUML:
                return new PlantUMLDocument();
            case MERMAID:
                return new MermaidDocument();
        }
    }
}
