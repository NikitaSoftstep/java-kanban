package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.HandlersException;
import task.Epic;
import task.Subtask;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {


    public EpicsHandler(TaskManager manager) {
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
                        case 2 -> getEpicTasks(exchange);
                        case 3 -> getEpicTask(exchange);
                        case 4 -> getEpicSubtasks(exchange);
                    }
                }
                case "POST" -> {
                    Epic taskFromJson = getEpicFromJson(exchange);
                    switch (pathLength) {
                        case 2 -> postEpic(exchange, taskFromJson);
                        case 3 -> updateEpic(exchange, taskFromJson);

                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> deleteEpics(exchange);
                        case 3 -> deleteEpic(exchange);
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
            throw new HandlersException("Исключение EpicsHandler", e);
        }
    }

    private void getEpicTasks(HttpExchange exchange) throws IOException {
        List<Epic> tasks = manager.getEpicTasks();
        if (tasks == null) {
            sendNotFound(exchange, "Задача не найдена");
        }
        String tasksJson = gson.toJson(tasks);
        sendText(exchange, tasksJson);
    }

    private void getEpicTask(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isEmpty()) {
            sendNotFound(exchange, "Задача не найдена");
        } else {
            Epic newTask = manager.getEpicTask(id.get());
            if (newTask == null) {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
        Epic task = manager.getEpicTask(id.get());
        String taskJson = gson.toJson(task);
        sendText(exchange, taskJson);
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> epicId = getTaskId(exchange);
        if (epicId.isEmpty()) {
            sendNotFound(exchange, "Задача не найдена");
        } else {
            if (manager.getEpicTask(epicId.get()) != null) {
                ArrayList<Subtask> subtasks = manager.getEpicSubtasks(epicId.get());
                String subtasksJson = gson.toJson(subtasks);
                sendText(exchange, subtasksJson);
            }
        }
    }

    private Epic getEpicFromJson(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        JsonElement element = JsonParser.parseString(stringInput);
        if (!element.isJsonObject()) {
            sendNotAcceptable(exchange, "Это не Json объект");
        }
        JsonObject jsonObject = element.getAsJsonObject();
        Epic taskFromJson = gson.fromJson(jsonObject, Epic.class);
        return taskFromJson;
    }

    private void postEpic(HttpExchange exchange, Epic taskFromJson) throws IOException {
        manager.addEpicTask(taskFromJson);
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    private void updateEpic(HttpExchange exchange, Epic taskFromJson) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            Epic task2 = manager.updateEpicTask(taskFromJson);
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

    private void deleteEpics(HttpExchange exchange) throws IOException {
        manager.deleteEpicTasks();
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> id = getTaskId(exchange);
        if (id.isPresent()) {
            if (manager.getEpicTask(id.get()) != null) {
                manager.deleteEpic(id.get());
                exchange.sendResponseHeaders(201, 0);
                exchange.close();
            } else {
                sendNotFound(exchange, "Задача не найдена");
            }
        }
    }
}
