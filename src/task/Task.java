package task;

import category.TaskStatus;


public class Task {
    private String title;
    private String description;
    private int taskID;
    private TaskStatus status;
    private TaskTypes taskType = TaskTypes.TASK;

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
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
        // id,type,name,status,description,epic
        return String.format("%s,%s,%s,%s,%s,", taskID,
                TaskTypes.TASK,
                title,
                status,
                description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return this.getTaskID() == task.getTaskID();
    }


}