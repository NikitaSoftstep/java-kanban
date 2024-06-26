package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.util.List;


public class HistoryHandler extends BaseHttpHandler implements HttpHandler {


    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {

                List<Task> history = manager.getHistory();
                String historyJson = gson.toJson(history);
                sendText(exchange, historyJson);
            } else {
                sendNotFound(exchange, "Method not found");
            }
            exchange.close();
        } catch (IOException e) {
            System.out.println("Возникло исключение: " + e.getMessage());
            System.out.println("Подробности об ошибке:");
            e.printStackTrace();
        }
    }

}
