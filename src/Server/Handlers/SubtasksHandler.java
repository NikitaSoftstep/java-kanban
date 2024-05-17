package Server.Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Subtask;
import taskmanager.TaskManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {


    TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) {
        try {
            Gson gson = createGson();
            String method = exchange.getRequestMethod();
            int pathLength = getPathLength(exchange);
            switch (method) {
                case "GET" -> {
                    switch (pathLength) {
                        case 2 -> {
                            List<Subtask> tasks = manager.getSubtasks();
                            if (tasks == null) {
                                sendNotFound(exchange, "Задача не найдена");
                            }
                            String tasksJson = gson.toJson(tasks);
                            sendText(exchange, tasksJson);
                        }
                        case 3 -> {
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
                    }
                }
                case "POST" -> {
                    InputStream input = exchange.getRequestBody();
                    String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                    JsonElement element = JsonParser.parseString(stringInput);
                    if (!element.isJsonObject()) {
                        sendNotAcceptable(exchange, "Это не Json объект");
                    }
                    JsonObject jsonObject = element.getAsJsonObject();
                    Subtask taskFromJson = gson.fromJson(jsonObject, Subtask.class);
                    switch (pathLength) {
                        case 2 -> {
                            Subtask newTask = manager.addSubtask(taskFromJson);
                            if (newTask == null) {
                                sendNotAcceptable(exchange, "Задача пересекается по времени");
                            } else {
                                exchange.sendResponseHeaders(201, 0);
                                exchange.close();
                            }
                        }
                        case 3 -> {
                            Optional<Integer> id = getTaskId(exchange);
                            if (id.isPresent()) {
                                Subtask task2 = manager.updateSubtask(taskFromJson);
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
                    }
                }
                case "DELETE" -> {
                    switch (pathLength) {
                        case 2 -> {
                            manager.deleteSubtasks();
                            exchange.sendResponseHeaders(201, 0);
                            exchange.close();
                        }
                        case 3 -> {
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
            System.out.println("Возникло исключение");
        }

    }

}