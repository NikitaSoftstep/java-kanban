package taskmanager;

public class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }
}
