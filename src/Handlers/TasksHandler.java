package Handlers;

import Handlers.TypeTokens.TasksToken;
import category.TaskStatus;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        System.out.println(pathLength);


            if (method.equals("GET")) {
                switch (pathLength) {
                    case 2 -> {
                        List<Task> tasks = manager.getSimpleTasks();
                        System.out.println(tasks);
                        System.out.println("отправили запрос на sendtext");
                        if (tasks == null) {
                            System.out.println("ой, у нас налл!");
                            baseHttpHandler.sendNotFound(exchange);
                            return;
                        }

                        String tasksJson = gson.toJson(tasks);
                        baseHttpHandler.sendText(exchange, tasksJson);
                        System.out.println("или здесь?");
                    }
                    case 3 -> {
                        Optional<Integer> id = baseHttpHandler.getTaskId(exchange);
                        if (id.isEmpty()) {
                            baseHttpHandler.sendNotFound(exchange);
                            return;
                        }
                        Task task = manager.getSimpleTask(id.get());
                        String taskJson = gson.toJson(task);
                        baseHttpHandler.sendText(exchange, taskJson);
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
