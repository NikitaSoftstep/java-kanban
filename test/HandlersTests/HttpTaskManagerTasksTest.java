package HandlersTests;

import Server.Handlers.BaseHttpHandler;
import Server.HttpTaskServer;
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
import java.time.LocalDateTime;
import java.util.List;

import taskmanager.InMemoryTaskManager;
import taskmanager.TaskManager;
import task.Task;
import task.TaskTypes;
import category.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class HttpTaskManagerTasksTest extends BaseHttpHandler {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор Server.HttpTaskServer
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    HttpServer server = httpTaskServer.createServer();


    Gson gson = createGson();

    public HttpTaskManagerTasksTest() throws IOException {
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
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Instant.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getSimpleTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }
}