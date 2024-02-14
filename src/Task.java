public class Task {
    private String title;
    private String description;
    private TaskCategory status;
    private int taskID;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void setCategory(TaskCategory taskCategory) {
        status = taskCategory;
    }

    public TaskCategory getCategory() {
        return status;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", taskID=" + taskID +
                '}';
    }
}