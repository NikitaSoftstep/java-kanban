package tasks;

import task_category.TaskCategory;

public class Task {
    private String title;
    private String description;
    private int taskID;
    private TaskCategory category;

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

    @Override
    public String toString() {
        return "tasks.Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskID=" + taskID +
                ", category=" + category +
                '}';
    }
}