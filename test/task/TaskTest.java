package task;

import category.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void testEqualityByID() {
        Task task1 = new Task("Заголовок1", "описание1", TaskStatus.NEW);
        task1.setTaskID(1);
        Task task2 = new Task("Заголовок2", "описание2", TaskStatus.NEW);
        task2.setTaskID(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }
}
