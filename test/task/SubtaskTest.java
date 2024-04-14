package task;

import category.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    public void testEqualityByID() {
        Subtask task1 = new Subtask("Заголовок3", "описание3", TaskStatus.NEW);
        task1.setTaskID(1);
        Subtask task2 = new Subtask("Заголовок4", "описание4", TaskStatus.NEW);
        task2.setTaskID(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }

    @Test
    public void subtaskCantBeEpicToItself() {
        // возможность не предусмотрена кодом
    }
}