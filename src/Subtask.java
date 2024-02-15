public class Subtask extends Task {

    private int epicID;
    public Subtask(String title, String description) {
        super(title, description);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
}