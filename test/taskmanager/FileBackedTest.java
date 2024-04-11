package taskmanager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTest extends TaskManagerTest<FileBackedTaskManager> {

    String savePath = "temp.csv";
    private final FileBackedTaskManager fileBackedTaskManager = createTaskManager();

    public FileBackedTest() {
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File(savePath));
    }

    @BeforeEach
    public void before() {
        createTaskManager();
    }
    @AfterEach
    public void after() throws IOException {
        Files.deleteIfExists(Path.of(savePath));
    }

    @Test
    void testSave() throws IOException {
        Task task = new Task("title", "description");
        fileBackedTaskManager.addSimpleTask(task);
        List<String> list = Files.readAllLines(Path.of(savePath));
        assertTrue(list.contains(task.toString()));
    }

    @Test
    void testLoadFromFile() throws IOException {
        Epic epic = new Epic("title", "descr");
        fileBackedTaskManager.addEpicTask(epic);
        int id = epic.getTaskID();
        fileBackedTaskManager.getEpicTask(id);
        List<String> list = Files.readAllLines(Path.of(savePath));
        String lastLine = list.getLast();
        char firstChar = lastLine.charAt(0);
        assertEquals(id, Character.getNumericValue(firstChar));
    }
}
