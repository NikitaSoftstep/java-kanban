package HandlersTests;

import server.handlers.BaseHttpHandler;
import server.HttpTaskServer;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import taskmanager.Managers;
import taskmanager.TaskManager;
import task.Task;
import category.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class TasksHandlerTest extends BaseHttpHandler {
    TaskManager manager = Managers.getDefaultManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    HttpServer server = httpTaskServer.createServer();


    Gson gson = createGson();

    public TasksHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteSimpleTasks();
        manager.deleteSubtasks();
        manager.deleteEpicTasks();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop(8);
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {


        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Instant.now(), Duration.ofMinutes(5));
        task.setTaskID(1);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getSimpleTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }
}