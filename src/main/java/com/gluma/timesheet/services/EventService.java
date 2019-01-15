package com.gluma.timesheet.services;

import com.gluma.timesheet.dao.EventDAO;
import com.gluma.timesheet.datamdodel.Event;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class EventService extends Service<ObservableList<Event>> {
    private static LocalDate localDate;

    public EventService(LocalDate localDate){
        EventService.localDate = localDate;
    }

    @Override
    protected Task<ObservableList<Event>> createTask() {
        return new Task<>() {
            @Override
            protected ObservableList<Event> call() throws Exception {
                ObservableList eventsToLoad = EventDAO.searchEvents(localDate);
                return eventsToLoad;
            }
        };
    }
}
