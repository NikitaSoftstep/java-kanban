package taskmanager;

import category.TaskCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;
class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void checkIfPrevTaskAndItsDataSaved() {
        Task task1 = new Task("заглавие", "описание");
        task1.setCategory(TaskCategory.NEW);
        task1.setTaskID(10);
        historyManager.add(task1);
        Task task2 = new Task("заглавие1", "описание1");
        task2.setCategory(TaskCategory.DONE);
        task2.setTaskID(11);
        historyManager.add(task2);
        Task target = historyManager.getHistory().get(0);
        Assertions.assertEquals(target.getTaskID(), 10);
        Assertions.assertEquals(target.getTitle(), "заглавие");
        Assertions.assertEquals(target.getDescription(), "описание");
        Assertions.assertEquals(target.getCategory(), TaskCategory.NEW);
    }
}