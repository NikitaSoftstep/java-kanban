package server.handlers;

import server.handlers.adapters.DurationAdapter;
import server.handlers.adapters.InstantAdapter;
import server.handlers.adapters.TaskTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import task.TaskTypes;
import taskmanager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class BaseHttpHandler {

    protected TaskManager manager;

    public static Gson gson = createGson();

    public static Gson createGson() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(TaskTypes.class, new TaskTypeAdapter())
                .setPrettyPrinting()
                .create();
        return gson;
    }


    protected void sendText(HttpExchange exchange, String text) throws IOException {

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, text.getBytes().length);
            os.write(text.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(404, resp.length);
            os.write(resp);
        } finally {
            exchange.close();
        }
    }

    protected void sendNotAcceptable(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(406, resp.length);
            os.write(resp);
        } finally {
            exchange.close();
        }
    }

    protected int getPathLength(HttpExchange exchange) {
        String[] pathLength = exchange.getRequestURI().getPath().split("/");
        return pathLength.length;
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(splitPath[2]));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }


}
