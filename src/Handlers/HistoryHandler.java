package Handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.util.List;


public class HistoryHandler implements HttpHandler {

    TaskManager manager;
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
    Gson gson = baseHttpHandler.createGson();


    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {

                List<Task> history = manager.getHistory();
                String historyJson = gson.toJson(history);
                baseHttpHandler.sendText(exchange, historyJson);
            } else {
                baseHttpHandler.sendNotFound(exchange, "Method not found");
            }
            exchange.close();
        } catch (IOException e) {
            System.out.println("Возникло исключение");
        }
     }

}
