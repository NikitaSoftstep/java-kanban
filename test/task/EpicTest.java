package task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    public void testEqualityByID() {
        Epic task1 = new Epic("Заголовок2", "описание2");
        task1.setTaskID(1);
        Epic task2 = new Epic("Заголовок3", "описание3");
        task2.setTaskID(1);
        Assertions.assertEquals(task1, task2, "Объекты не равны!");
    }

    @Test
    public void epicCantBeAddedToItself() {
        // возможность не предусмотрена кодом
    }
}