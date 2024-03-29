package taskmanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Task;
class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static Task task1;
    private static Task task2;

    @BeforeAll
    public static void beforeAll(){
        task1 = new Task("задача 1", "описание 1");
        task2 = new Task("задача 2", "описание 2");
        task1.setTaskID(1);
        task2.setTaskID(2);
    }

    @Test
    public void isEntryAdded() {
        historyManager.add(task1);
        Assertions.assertEquals(1, historyManager.getHistory().size()); // проверка изменения размера
        Assertions.assertEquals(task1, historyManager.getHistory().get(0));
        historyManager.add(task2);
        Assertions.assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(task1.getTaskID());
        Assertions.assertEquals(1, historyManager.getHistory().size());

    }

}