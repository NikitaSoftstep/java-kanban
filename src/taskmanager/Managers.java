package taskmanager;


import java.io.File;
import java.io.IOException;

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
