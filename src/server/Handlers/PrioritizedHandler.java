package server.Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanager.TaskManager;
import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;
    Gson gson = createGson();

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String prioritizedJson = gson.toJson(prioritizedTasks);
            sendText(exchange, prioritizedJson);
        } else {
            sendNotFound(exchange, "Method not found");
        }
        exchange.close();
    }
}
