package task;

import category.TaskStatus;

import java.time.*;
import java.util.ArrayList;

public class Epic extends Task {

    protected TaskTypes epicType = TaskTypes.EPIC;
    protected ArrayList<Integer> subtaskIDs = new ArrayList<>();


    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);

    }


    public void addSubtaskID(int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic task = (Epic) o;
        return this.getTaskID() == task.getTaskID();
    }

    @Override
    public String toString() {
        ZonedDateTime zone;
        int duration1;
        if (!(getStartTime() == null) && !(getDuration() == null)) {
            LocalDateTime local = LocalDateTime.ofInstant(getStartTime(), ZoneOffset.UTC);
            duration1 = (int) getDuration().toMinutes();
            return String.format("%s,%s,%s,%s,%s,%s,%s", getTaskID(),
                    TaskTypes.EPIC,
                    getTitle(),
                    getDescription(),
                    getStatus(),
                    formatter.format(local),
                    duration1);
        } else {
            // id,type,name,status,description,epic
            return String.format("%s,%s,%s,%s,%s,%s,%s", getTaskID(),
                    TaskTypes.EPIC,
                    getTitle(),
                    getDescription(),
                    getStatus(),
                    "null",
                    "null");
        }
    }
}