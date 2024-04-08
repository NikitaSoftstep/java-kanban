package taskmanager;

import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(Path savedPath) {
        return new FileBackedTaskManager(savedPath);
    }

    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }
}
