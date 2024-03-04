package task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    public void testEqualityByID() {
        Task task1 = new Task("Заголовок1", "описание1");
        task1.setTaskID(1);
        Task task2 = new Task("Заголовок2", "описание2");
        task2.setTaskID(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }
}
