package server.handlers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.HandlersException;
import task.Task;
import taskmanager.TaskManager;

import java.io.*;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {


    public TasksHandler(TaskManager manager) {
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
                        case 2 -> getTasks(exchange);
                        case 3 -> getTask(exchange);
                    }
                }
                case "POST" -> {
                    Task taskFromJson = getTaskFromJson(exchange);
                    switch (pathLength) {
                        case 2 -> postTask(exchange, taskFromJson);
                        case 3 -> updateTask(exchange, taskFromJson);
                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> deleteTasks(exchange);
                        case 3 -> deleteTask(exchange);
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
            throw new HandlersException("Исключение TasksHandlers", e);
        }

    }

    private void getTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = manager.getSimpleTasks();
        if (tasks == null) {
            sendNotFound(exchange, "Задача не найдена");
        }
        String tasksJson = gson.toJson(tasks);
        sendText(exchange, tasksJson);
    }

    private void getTask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isEmpty()) {
            sendNotFound(exchange, "Задача не найдена");
        } else {
            Task newTask = manager.getSimpleTask(id.get());
            if (newTask == null) {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
        Task task = manager.getSimpleTask(id.get());
        String taskJson = gson.toJson(task);
        sendText(exchange, taskJson);
    }

    private Task getTaskFromJson(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement element = JsonParser.parseString(stringInput);
        if (!element.isJsonObject()) {
            sendNotAcceptable(exchange, "Это не Json объект");
        }
        JsonObject jsonObject = element.getAsJsonObject();
        Task taskFromJson = gson.fromJson(jsonObject, Task.class);
        return taskFromJson;
    }

    private void postTask(HttpExchange exchange, Task taskFromJson) throws IOException {
        Task newTask = manager.addSimpleTask(taskFromJson);
        if (newTask == null) {
            sendNotAcceptable(exchange, "Задача пересекается по времени");
        } else {
            exchange.sendResponseHeaders(201, 0);
            exchange.close();
        }
    }

    private void updateTask(HttpExchange exchange, Task taskFromJson) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            Task task2 = manager.updateSimpleTask(taskFromJson);
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

    private void deleteTasks(HttpExchange exchange) throws IOException {
        manager.deleteSimpleTasks();
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            if (manager.getSimpleTask(id.get()) != null) {
                manager.deleteSimpleTask(id.get());
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
    }
}
