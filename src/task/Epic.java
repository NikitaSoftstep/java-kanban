package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSubtaskID(int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.EPIC;
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
                getCategory(),
                getDescription());
    }
}