package task;

import category.TaskCategory;


public class Task {
    private String title;
    private String description;
    private int taskID;
    private TaskCategory category;

    private int epicID = 0;

    private TaskTypes taskType = TaskTypes.TASK;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
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

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public TaskTypes getType() {
        return taskType;
    }

    @Override
    public String toString() {
        // id,type,name,status,description,epic
        return String.format("%s,%s,%s,%s,%s,", taskID,
                TaskTypes.TASK,
                title,
                category,
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