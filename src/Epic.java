import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> subtaskIDs = new ArrayList<>();
    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSutaskID (int subtaskID) {
        subtaskIDs.add(subtaskID);
    }

    public ArrayList<Integer> getSubtaskIDs() {
        return subtaskIDs;
    }
}