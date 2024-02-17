package tasks;

import tasks.Task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIDs = new ArrayList<>();
    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSubtaskID (int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }
}