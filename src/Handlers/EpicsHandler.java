package Handlers;

import Handlers.TypeTokens.EpicsToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.FileBackedTaskManager;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EpicsHandler implements HttpHandler {

    TaskManager manager;
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
    public EpicsHandler(TaskManager manager) {
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
                            List<Epic> tasks = manager.getEpicTasks();
                            if (tasks == null) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                            }
                            String tasksJson = gson.toJson(tasks);
                            baseHttpHandler.sendText(exchange, tasksJson);
                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isEmpty()) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                            } else {
                                Epic newTask = manager.getEpicTask(id.get());
                                if (newTask == null) {
                                    baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                                }
                            }
                            Epic task = manager.getEpicTask(id.get());
                            String taskJson = gson.toJson(task);
                            baseHttpHandler.sendText(exchange, taskJson);
                        }
                        case 4 -> {
                            Optional<Integer> epicId = baseHttpHandler.getTaskId(exchange);
                            if (epicId.isEmpty()) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                            } else {
                                if (manager.getEpicTask(epicId.get()) != null) {
                                    ArrayList<Subtask> subtasks = manager.getEpicSubtasks(epicId.get());
                                    String subtasksJson = gson.toJson(subtasks);
                                    baseHttpHandler.sendText(exchange, subtasksJson);
                                }
                            }
                        }
                    }
                }
                case "POST" -> {
                    InputStream input = exchange.getRequestBody();
                    String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement element = JsonParser.parseString(stringInput);
                    if (!element.isJsonObject()) {
                        baseHttpHandler.sendNotAcceptable(exchange, "Это не Json объект");
                    }
                    JsonObject jsonObject = element.getAsJsonObject();
                    Epic taskFromJson = gson.fromJson(jsonObject, Epic.class);
                    switch (pathLength) {
                        case 2 -> {
                            manager.addEpicTask(taskFromJson);
                            exchange.sendResponseHeaders(201, 0);
                            exchange.close();
                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isPresent()) {
                                Epic task2 = manager.updateEpicTask(taskFromJson);
                                if (task2 != null) {
                                        exchange.sendResponseHeaders(201, 0);
                                        exchange.close();
                                } else {
                                    baseHttpHandler.sendNotAcceptable(exchange, "Возникло пересечение");
                                }
                            } else {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                            }
                        }
                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> {
                            manager.deleteEpicTasks();
                            exchange.sendResponseHeaders(201, 0);
                            exchange.close();

                        }
                        case 3 -> {
                            Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                            if (id.isPresent()) {
                                if (manager.getEpicTask(id.get()) != null) {
                                    manager.deleteEpic(id.get());
                                    exchange.sendResponseHeaders(201, 0);
                                    exchange.close();
                                } else {
                                    baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
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
                    exchange.close();
                }

            }
        } catch (Exception  e) {
            System.out.println("Возникло исключение");
        }

    }
}
