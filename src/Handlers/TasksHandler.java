package Handlers;

import Handlers.TypeTokens.TasksToken;
import category.TaskStatus;
import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import task.Task;
import taskmanager.FileBackedTaskManager;
import taskmanager.TaskManager;

import java.io.*;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TasksHandler implements HttpHandler {

    TaskManager manager;
    BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(HttpExchange exchange) {
        try {
            Gson gson = baseHttpHandler.createGson();
            String method = exchange.getRequestMethod();
            int pathLength = baseHttpHandler.getPathLength(exchange);


            if (method.equals("GET")) {
                switch (pathLength) {
                    case 2 -> {
                        List<Task> tasks = manager.getSimpleTasks();
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
                            Task newTask = manager.getSimpleTask(id.get());
                            if (newTask == null) {
                                baseHttpHandler.sendNotFound(exchange, "Задача не найдена");
                                return;
                            }
                        }
                        Task task = manager.getSimpleTask(id.get());
                        String taskJson = gson.toJson(task);
                        baseHttpHandler.sendText(exchange, taskJson);
                    }
                }
            } else if (method.equals("POST")) {
                InputStream input = exchange.getRequestBody();
                String stringInput = new String(input.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement element = JsonParser.parseString(stringInput);
                if (!element.isJsonObject()) {
                    baseHttpHandler.sendNotAcceptable(exchange, "Это не Json объект");
                    return;
                }
                JsonObject jsonObject = element.getAsJsonObject();
                Task taskFromJson = gson.fromJson(jsonObject, Task.class);
                switch (pathLength) {
                    case 2 -> {
                        Task newTask = manager.addSimpleTask(taskFromJson);
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
                            Task task2 = manager.updateSimpleTask(taskFromJson);
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
            } else if (method.equals("DELETE")) {
                switch (pathLength) {
                    case 2 -> {
                        manager.deleteSimpleTasks();
                        try (OutputStream os = exchange.getResponseBody()) {
                            exchange.sendResponseHeaders(201, 0);
                        }
                    }
                    case 3 -> {
                        Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                        if (id.isPresent()) {
                            if (manager.getSimpleTask(id.get()) != null) {
                                manager.deleteSimpleTask(id.get());
                                try (OutputStream os = exchange.getResponseBody()) {
                                    exchange.sendResponseHeaders(201, 0);
                                }
                            }
                        }
                    }
                }
            } else {
                String errorMessage = "Возникла ошибка обработки запроса";
                exchange.sendResponseHeaders(500, errorMessage.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorMessage.getBytes());
                }
            }
        } catch (Exception  e) {
            System.out.println("Возникло исключение");
        }

    }
}
