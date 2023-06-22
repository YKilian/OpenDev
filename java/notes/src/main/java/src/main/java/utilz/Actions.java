package src.main.java.utilz;

public enum Actions {
    SAVE("Save"),
    DELETE("Delete"),
    UNDO("Undo"),
    SAVESTATUS("SaveStatus"),
    EXPORT("Export");

    private final String action;

    Actions(String action){
        this.action = action;
    }

    public String getAction(){
        return this.action;
    }
}
