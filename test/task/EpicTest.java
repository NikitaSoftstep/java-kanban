package task;

import category.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    public void testEqualityByID() {
        Epic task1 = new Epic("Заголовок2", "описание2", TaskStatus.NEW);
        task1.setTaskID(1);
        Epic task2 = new Epic("Заголовок3", "описание3", TaskStatus.NEW);
        task2.setTaskID(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }

    @Test
    public void epicCantBeAddedToItself() {
        // возможность не предусмотрена кодом
    }
}