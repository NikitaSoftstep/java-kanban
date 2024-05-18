package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.HandlersException;
import task.Subtask;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {


    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            int pathLength = getPathLength(exchange);
            switch (method) {
                case "GET" -> {
                    switch (pathLength) {
                        case 2 -> getSubtasks(exchange);
                        case 3 -> getSubtask(exchange);
                    }
                }
                case "POST" -> {
                    Subtask taskFromJson = getSubtaskFromJson(exchange);
                    switch (pathLength) {
                        case 2 -> postSubtask(exchange, taskFromJson);
                        case 3 -> updateSubtask(exchange, taskFromJson);
                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> deleteSubtasks(exchange);
                        case 3 -> deleteSubtask(exchange);
                    }
                }
                default -> {
                    String errorMessage = "Возникла ошибка обработки запроса";
                    exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorMessage.getBytes());
                    }
                    exchange.close();
                }
            }
        } catch (Exception e) {
            throw new HandlersException("Исключение SubtasksHandler", e);
        }

    }

    private void getSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> tasks = manager.getSubtasks();
        if (tasks == null) {
            sendNotFound(exchange, "Задача не найдена");
        }
        String tasksJson = gson.toJson(tasks);
        sendText(exchange, tasksJson);
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isEmpty()) {
            sendNotFound(exchange, "Задача не найдена");
        } else {
            Subtask newTask = manager.getSubtask(id.get());
            if (newTask == null) {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
        Subtask task = manager.getSubtask(id.get());
        String taskJson = gson.toJson(task);
        sendText(exchange, taskJson);
    }

    private Subtask getSubtaskFromJson(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement element = JsonParser.parseString(stringInput);
        if (!element.isJsonObject()) {
            sendNotAcceptable(exchange, "Это не Json объект");
        }
        JsonObject jsonObject = element.getAsJsonObject();
        Subtask taskFromJson = gson.fromJson(jsonObject, Subtask.class);
        return taskFromJson;
    }

    private void postSubtask(HttpExchange exchange, Subtask subtaskFromJson) throws IOException {
        Subtask newTask = manager.addSubtask(subtaskFromJson);
        if (newTask == null) {
            sendNotAcceptable(exchange, "Задача пересекается по времени");
        } else {
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        }
    }

    private void updateSubtask(HttpExchange exchange, Subtask subtaskFromJson) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            Subtask task2 = manager.updateSubtask(subtaskFromJson);
            if (task2 != null) {
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                sendNotAcceptable(exchange, "Возникло пересечение");
            }
        } else {
            sendNotFound(exchange, "Задача не найдена");
        }
    }

    private void deleteSubtasks(HttpExchange exchange) throws IOException {
        manager.deleteSubtasks();
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    private void deleteSubtask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            if (manager.getSubtask(id.get()) != null) {
                manager.deleteSubtask(id.get());
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
    }
}
