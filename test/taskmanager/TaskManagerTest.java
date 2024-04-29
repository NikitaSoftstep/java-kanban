package taskmanager;

import category.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {
    private T taskManager;


    @BeforeEach
    public void init() {
        taskManager = createTaskManager();
    }

    protected abstract T createTaskManager();

    @Test
    public void testAddTask() {
        Task tsk = new Task("заголовок", "описание", TaskStatus.NEW);
        taskManager.addSimpleTask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getSimpleTasks().size());
        Assertions.assertEquals(tsk, taskManager.getSimpleTask(tskID));
    }

    @Test
    public void checkIfEpicAddedAndRetrieved() {
        Epic tsk = new Epic("заголовок", "описание", TaskStatus.NEW);
        taskManager.addEpicTask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getEpicTasks().size());
        Assertions.assertEquals(tsk, taskManager.getEpicTask(tskID));
    }

    @Test
    public void checkIfSubtaskAddedAndRetrieved() {
        Epic epic = new Epic("title", "description", TaskStatus.NEW);
        epic.setStatus(TaskStatus.NEW);
        taskManager.addEpicTask(epic);
        int epicID = epic.getTaskID();
        Subtask tsk = new Subtask("заголовок", "описание", TaskStatus.NEW, Instant.now(), Duration.ofMinutes(10));
        tsk.setStatus(TaskStatus.NEW);
        tsk.setEpicID(epicID);
        taskManager.addSubtask(tsk);
        int tskID = tsk.getTaskID();
        Assertions.assertEquals(1, taskManager.getSubtasks().size());
        Assertions.assertEquals(tsk, taskManager.getSubtask(tskID));
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingTask() {
        Task task = new Task("заголовок", "описание", TaskStatus.NEW);
        task.setStatus(TaskStatus.NEW);
        taskManager.addSimpleTask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingEpic() {
        Epic task = new Epic("заголовок", "описание", TaskStatus.NEW);
        task.setStatus(TaskStatus.NEW);
        taskManager.addEpicTask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    public void checkEqualityBeforeAndAfterAddingSubtask() {
        Subtask task = new Subtask("заголовок", "описание", TaskStatus.NEW);
        task.setStatus(TaskStatus.NEW);
        taskManager.addSubtask(task);
        Assertions.assertEquals("заголовок", task.getTitle());
        Assertions.assertEquals("описание", task.getDescription());
        Assertions.assertEquals(TaskStatus.NEW, task.getStatus());
    }





}