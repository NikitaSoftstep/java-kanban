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

import task.Subtask;
import taskmanager.Managers;
import taskmanager.TaskManager;
import category.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;


public class SubtasksHandlerTest extends BaseHttpHandler {
    TaskManager manager = Managers.getDefaultManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    HttpServer server = httpTaskServer.createServer();


    Gson gson = createGson();

    public SubtasksHandlerTest() {
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
    public void testGetEpic() throws IOException, InterruptedException {
        try {
            Subtask subtask = new Subtask("Test 3", "Testing task 2",
                    TaskStatus.NEW, Instant.now(), Duration.ofMinutes(5));
            manager.addSubtask(subtask);


            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());

            List<Subtask> subtasks = manager.getSubtasks();

            assertTrue(subtasks.isEmpty(), "Задачи не возвращаются");

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}