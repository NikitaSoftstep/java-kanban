package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubtasksHandler implements HttpHandler {


    TaskManager manager;
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler();

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) {
        try {
            Gson gson = baseHttpHandler.createGson();
            String method = exchange.getRequestMethod();
            int pathLength = baseHttpHandler.getPathLength(exchange);


            switch (method) {
                case "GET" -> {
                    switch (pathLength) {
                        case 2 -> {
                            List<Subtask> tasks = manager.getSubtasks();
                            if (tasks == null) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                                return;
                            }
                            String tasksJson = gson.toJson(tasks);
                            baseHttpHandler.sendText(exchange, tasksJson);
                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isEmpty()) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                                return;
                            } else {
                                Subtask newTask = manager.getSubtask(id.get());
                                if (newTask == null) {
                                    baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                                    return;
                                }
                            }
                            Subtask task = manager.getSubtask(id.get());
                            String taskJson = gson.toJson(task);
                            baseHttpHandler.sendText(exchange, taskJson);
                        }
                    }
                }
                case "POST" -> {
                    InputStream input = exchange.getRequestBody();
                    String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement element = JsonParser.parseString(stringInput);
                    if (!element.isJsonObject()) {
                        baseHttpHandler.sendNotAcceptable(exchange, "Это не Json объект");
                        return;
                    }
                    JsonObject jsonObject = element.getAsJsonObject();
                    Subtask taskFromJson = gson.fromJson(jsonObject, Subtask.class);
                    switch (pathLength) {
                        case 2 -> {
                            Subtask newTask = manager.addSubtask(taskFromJson);
                            if (newTask == null) {
                                baseHttpHandler.sendNotAcceptable(exchange, "Задача пересекается по времени");
                            } else {
                                try (OutputStream os = exchange.getResponseBody()) {
                                    exchange.sendResponseHeaders(201, 0);
                                }
                            }
                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isPresent()) {
                                Subtask task2 = manager.updateSubtask(taskFromJson);
                                if (task2 != null) {
                                    try (OutputStream os = exchange.getResponseBody()) {
                                        exchange.sendResponseHeaders(201, 0);
                                    }
                                } else {
                                    baseHttpHandler.sendNotAcceptable(exchange, "Возникло пересечение");
                                }
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                            }
                        }
                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> {
                            manager.deleteSubtasks();
                            try (OutputStream os = exchange.getResponseBody()) {
                                exchange.sendResponseHeaders(201, 0);
                            }
                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isPresent()) {
                                if (manager.getSubtask(id.get()) != null) {
                                    manager.deleteSubtask(id.get());
                                    try (OutputStream os = exchange.getResponseBody()) {
                                        exchange.sendResponseHeaders(201, 0);
                                    }
                                }
                            }
                        }
                    }
                }
                default -> {
                    String errorMessage = "Возникла ошибка обработки запроса";
                    exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(errorMessage.getBytes());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Возникло исключение");
        }

    }

}
