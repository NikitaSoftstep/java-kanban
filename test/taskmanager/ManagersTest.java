package taskmanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {



    @Test
    public void returnsReadyTaskManager() {
        InMemoryTaskManager taskManager = Managers.getDefaultTask();
        Assertions.assertFalse(null == taskManager, "Объект - null!");
        Assertions.assertTrue(taskManager instanceof InMemoryTaskManager,
                "Объект не готов!");
    }

    @Test
    public void returnsReadyHistoryManager() {
        HistoryManager taskManager1 = Managers.getDefaultHistory();
        Assertions.assertFalse(null == taskManager1, "Объект - null!");
        Assertions.assertTrue(taskManager1 instanceof InMemoryHistoryManager,
                "Объект не готов!");
    }

    @Test
    public void returnsReadyDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertFalse(null == taskManager, "Объект - null!");
        Assertions.assertTrue(taskManager instanceof TaskManager,
                "Объект не готов!");
    }
}