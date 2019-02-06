package com.gluma.timesheet.datamdodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Task implements BaseModel{
    private SimpleIntegerProperty idTask;
    private SimpleStringProperty name;
    private SimpleStringProperty descirption;

    public Task(String name){
        this.name = new SimpleStringProperty(name);
    }

    public Task(String name, String descirption) {
        this(name);
        this.descirption = new SimpleStringProperty(descirption);
    }

    public Task(int idTask, String name, String description) {
        this(name,description);
        this.idTask = new SimpleIntegerProperty(idTask);
    }

    public int getIdTask() {
        return idTask.get();
    }

    public void setIdTask(int idTask) {
        this.idTask.set(idTask);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescirption() {
        return descirption.get();
    }

    public void setDescirption(String descirption) {
        this.descirption.set(descirption);
    }

    @Override
    public String toString() {
        return name.get();
    }
}
