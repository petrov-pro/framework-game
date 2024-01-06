package ua.org.petroff.game.engine.entities;

import java.util.HashMap;
import ua.org.petroff.game.engine.entities.Interfaces.ActionInterface;

public class BodyDescriber<T> {
    
    public static final String BODY_FOOT = "foot";
    public static final String BODY = "body";

    private String name;
    private String type;
    private String group;
    private final HashMap<String, T> data = new HashMap();
    private ActionInterface model;

    public BodyDescriber(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public BodyDescriber(String name, String type, String group) {
        this.name = name;
        this.type = type;
        this.group = group;
    }
    
    public BodyDescriber(String name, String type, String group, ActionInterface model) {
        this.name = name;
        this.type = type;
        this.group = group;
        this.model = model;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addData(String key, T value) {
        data.put(key, value);
    }

    public T getData(String key) {
        return data.get(key);
    }

    public ActionInterface getModel() {
        return model;
    }

}
