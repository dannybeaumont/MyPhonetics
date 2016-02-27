package model;


import com.google.gson.JsonObject;

public class Definition {
    String definition;
    String partOfSpeech;
    public Definition(JsonObject definition){
        setDefinition(definition.get("definition").getAsString());
        setPartOfSpeech(definition.get("partOfSpeech").getAsString());
    }
    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }
}
