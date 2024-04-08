package taskmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File savedPath) throws IOException {
        return new FileBackedTaskManager(savedPath);
    }

    public static TaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }
}
