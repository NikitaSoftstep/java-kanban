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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic task = (Epic) o;
        return this.getTaskID() == task.getTaskID();
    }
}