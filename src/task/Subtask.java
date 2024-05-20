package task;

import category.TaskStatus;

import java.time.*;

public class Subtask extends Task {

    protected int epicID;


    public Subtask(String title, String description, TaskStatus status) {
        super(title, description, status);
        taskType = TaskTypes.SUBTASK;
    }

    public Subtask(String title, String description, TaskStatus status, Instant startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        taskType = TaskTypes.SUBTASK;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int id) {
        epicID = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask task = (Subtask) o;
        return this.getTaskID() == task.getTaskID();
    }

    @Override
    public String toString() {
        ZonedDateTime zone;
        int duration1;
        if (!(getStartTime() == null) && !(getDuration() == null)) {
            LocalDateTime local = LocalDateTime.ofInstant(getStartTime(), ZoneOffset.UTC);
            duration1 = (int) getDuration().toMinutes();
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getTaskID(),
                    TaskTypes.SUBTASK,
                    getTitle(),
                    getDescription(),
                    getStatus(),
                    formatter.format(local),
                    duration1,
                    epicID);
        } else {
            // id,type,name,status,description,epic
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", getTaskID(),
                    TaskTypes.SUBTASK,
                    getTitle(),
                    getDescription(),
                    getStatus(),
                    "null",
                    "null",
                    epicID);
        }
    }
}