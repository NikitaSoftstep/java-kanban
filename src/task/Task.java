package task;

import category.TaskStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;


public class Task {
    protected String title;
    protected String description;
    protected int taskID;
    protected TaskStatus status;
    protected TaskTypes taskType = TaskTypes.TASK;

    protected Duration duration;

    protected Instant startTime;

    protected Instant endTime;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }



    public Task(String title,
                String description,
                TaskStatus status,
                Instant startTime,
                Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }


    public Instant getEndTime() {
        endTime = startTime.plus(duration);
        return endTime;
    }

    public void setEndTime() {
        endTime = startTime.plus(duration);
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskTypes getType() {
        return taskType;
    }

    public void setType(TaskTypes type) {
        this.taskType = type;
    }

    @Override
    public String toString() {
        ZonedDateTime zone;
        int duration1;
        if (!(startTime == null) && !(duration == null)) {
            LocalDateTime local = LocalDateTime.ofInstant(startTime, ZoneOffset.UTC);
            duration1 = (int) duration.toMinutes();
            return String.format("%s,%s,%s,%s,%s,%s,%s", taskID,
                    TaskTypes.TASK,
                    title,
                    description,
                    status,
                    formatter.format(local),
                    duration1);
        } else {
            // id,type,name,status,description,epic
            return String.format("%s,%s,%s,%s,%s,%s,%s", taskID,
                    TaskTypes.TASK,
                    title,
                    description,
                    status,
                    "null",
                    "null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return this.getTaskID() == task.getTaskID();
    }


}