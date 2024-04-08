package task;

public class Subtask extends Task {



    public Subtask(String title, String description) {
        super(title, description);
    }


    @Override
    public TaskTypes getType() {
        return TaskTypes.SUBTASK;
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
                getCategory(),
                getDescription(),
                getEpicID());
    }
}