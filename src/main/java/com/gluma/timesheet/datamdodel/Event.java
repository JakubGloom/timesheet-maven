package com.gluma.timesheet.datamdodel;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.Timestamp;

public class Event extends RecursiveTreeObject<Event> implements BaseModel, Comparable<Event> {

    private SimpleIntegerProperty idEvent;
    private Timestamp startDate;
    private Timestamp endDate;
    private SimpleIntegerProperty time;
    private SimpleIntegerProperty isAccepted;
    private SimpleIntegerProperty idEmployee;

    private Task task;
    private Employee employee;

    public Event(int idEvent, Timestamp startDate, Timestamp endDate, int time, Task task) {
        this.idEvent = new SimpleIntegerProperty(idEvent);
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = new SimpleIntegerProperty(time);
        this.task = task;
    }

    public Event(Employee employee){
        this.employee=employee;
    }

    public Event(Timestamp startDate, Timestamp endDate, int time, int isAccepted, int idEmployee, Task task) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = new SimpleIntegerProperty(time);
        this.isAccepted = new SimpleIntegerProperty(isAccepted);
        this.idEmployee = new SimpleIntegerProperty(idEmployee);
        this.task = task;
    }

    public Event(Employee employee, int idEvent, Timestamp startDate, Timestamp endDate, int time, Task task){
        this(employee,idEvent,time,task);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Event(Employee employee, int idEvent, int time, Task task) {
        this.idEvent = new SimpleIntegerProperty(idEvent);
        this.time = new SimpleIntegerProperty(time);
        this.task = task;
        this.employee = employee;
    }

    public int getIdEvent() {
        return idEvent.get();
    }

    public void setIdEvent(int idEvent) {
        this.idEvent.set(idEvent);
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getTime() {
        return time.getValue();
    }

    public void setTime(int time) {
        this.time.set(time);
    }

    public int getIsAccepted() {
        return isAccepted.get();
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted.set(isAccepted);
    }

    public int getIdEmployee() {
        return idEmployee.get();
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee.set(idEmployee);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void addTime(int time){
        this.time.set(getTime()+time);
    }

    public String getFullName(){
        return employee.getFullName();
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", time=" + time +
                ", isAccepted=" + isAccepted +
                ", idEmployee=" + idEmployee +
                ", task=" + task +
                ", employee=" + employee +
                '}';
    }


    @Override
    public int compareTo(Event eventToCompare) {
        if(this.getStartDate().before(eventToCompare.getStartDate())){
            return 1;
        }else{
            if(this.getStartDate().after(eventToCompare.getStartDate())){
                return -1;
            }
            return 0;
        }
    }
}
