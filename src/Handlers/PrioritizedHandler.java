package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler implements HttpHandler {

    TaskManager manager;
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
    Gson gson = baseHttpHandler.createGson();

    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String prioritizedJson = gson.toJson(prioritizedTasks);
             baseHttpHandler.sendText(exchange, prioritizedJson);
        } else {
            baseHttpHandler.sendNotFound(exchange, "Method not found");
        }
        exchange.close();
    }
}
