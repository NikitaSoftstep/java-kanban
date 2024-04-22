package task;

import category.TaskStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Epic extends Task {

    private TaskTypes taskType = TaskTypes.EPIC;
    private TaskStatus status;
    private ArrayList<Integer> subtaskIDs = new ArrayList<>();

    private Duration duration;
    private Instant startTime;
    private Instant endTime;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);

    }

    public void calculateStartEndTimeAndDuration(Instant startTime,
                                                 Instant endTime,
                                                 Duration duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
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
        return String.format("%s,%s,%s,%s,%s,", getTaskID(),
                TaskTypes.EPIC,
                getTitle(),
                getStatus(),
                getDescription());
    }
}