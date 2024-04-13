package task;

import category.TaskStatus;

public class Subtask extends Task {

    private int epicID;
    private TaskTypes taskType = TaskTypes.SUBTASK;


    public Subtask(String title, String description, TaskStatus status) {
        super(title, description, status);
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
        return String.format("%s,%s,%s,%s,%s,%s", getTaskID(),
                TaskTypes.SUBTASK,
                getTitle(),
                getStatus(),
                getDescription(),
                getEpicID());
    }
}