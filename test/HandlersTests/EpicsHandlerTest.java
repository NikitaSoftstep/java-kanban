package HandlersTests;

import Server.Handlers.Adapters.DurationAdapter;
import Server.Handlers.Adapters.InstantAdapter;
import Server.Handlers.Adapters.TaskTypeAdapter;
import Server.Handlers.BaseHttpHandler;
import Server.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import task.Epic;
import task.TaskTypes;
import taskmanager.Managers;
import taskmanager.TaskManager;
import category.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class EpicsHandlerTest extends BaseHttpHandler {
    TaskManager manager = Managers.getDefaultManager();
    HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    HttpServer server = httpTaskServer.createServer();


    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .registerTypeAdapter(TaskTypes.class, new TaskTypeAdapter())
            .setPrettyPrinting()
            .create();

    public EpicsHandlerTest() throws IOException {
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
            Epic epic = new Epic("Test 3", "Testing task 2",
                    TaskStatus.NEW);
            epic.setDuration(Duration.ofMinutes(5));
            epic.setStartTime(Instant.now());
            epic.setTaskID(1);
            manager.addEpicTask(epic);


            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/epics/1");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            Epic epic1 = manager.getEpicTask(1);

            assertNotNull(epic1, "Задачи не возвращаются");
            assertEquals(1, epic1.getTaskID(), "Некорректный номер задачи");
            assertEquals("Test 3", epic1.getTitle(), "Некорректное имя задачи");
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getStackTrace());
        }
    }


}