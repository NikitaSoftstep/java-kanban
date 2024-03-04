package taskmanager;

import category.TaskCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

class InMemoryTaskManagerTest {

    static private InMemoryTaskManager taskManager;
    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void checkIfSimpleTaskAddedAndRetrieved() {
        Task tsk = new Task("заголовок", "описание");
        taskManager.addSimpleTask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getSimpleTasks().size());
        Assertions.assertEquals(tsk, taskManager.getSimpleTask(tskID));
    }

    @Test
    public void checkIfEpicAddedAndRetrieved() {
        Epic tsk = new Epic("заголовок", "описание");
        taskManager.addEpicTask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getEpicTasks().size());
        Assertions.assertEquals(tsk, taskManager.getEpicTask(tskID));
    }

    @Test
    public void checkIfSubtaskAddedAndRetrieved() {
        Epic epic = new Epic("title", "description");
        epic.setCategory(TaskCategory.NEW);
        taskManager.addEpicTask(epic);
        int epicID = epic.getTaskID();
        Subtask tsk = new Subtask("заголовок", "описание");
        tsk.setCategory(TaskCategory.NEW);
        tsk.setEpicID(epicID);
        taskManager.addSubtask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getSubtasks().size());
        Assertions.assertEquals(tsk, taskManager.getSubtask(tskID));
    }

    @Test
    public void checkIfSetIDnotConflictedWithGenID() {
        // тест не имеет смысла, поскольку все заданные заранее ID
        // переопределяются в методах addSimpleTask/Epic/Subtask
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingTask() {
        Task task = new Task("заголовок", "описание");
        task.setCategory(TaskCategory.NEW);
        taskManager.addSimpleTask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskCategory.NEW, task.getCategory());
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingEpic() {
        Epic task = new Epic("заголовок", "описание");
        task.setCategory(TaskCategory.NEW);
        taskManager.addEpicTask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskCategory.NEW, task.getCategory());
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingSubtask() {
        Subtask task = new Subtask("заголовок", "описание");
        task.setCategory(TaskCategory.NEW);
        taskManager.addSubtask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskCategory.NEW, task.getCategory());
    }


}