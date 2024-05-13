package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TasksHandler implements HttpHandler {

    TaskManager manager;
    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream input = exchange.getRequestBody();
        String stringBody = new String(input.readAllBytes(), StandardCharsets.UTF_8);

        // ветвление через УЗНАТЬ ДЛИНУ ПУТИ, ЕСЛИ 3 - ТО ГЕТ ПО АЙДИ ИЛИ ДИЛИТ ПО АЙДИ, ЕСЛИ 2 - ТО ГЕТ АЛЛ ИЛИ ПОСТ
    }
}
