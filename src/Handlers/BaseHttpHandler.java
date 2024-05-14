package Handlers;

import Handlers.Adapters.DurationAdapter;
import Handlers.Adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;

public class BaseHttpHandler {

    protected Gson createGson() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .setPrettyPrinting()
                .create();
        return gson;
    }



    protected void sendText(HttpExchange exchange, String text) throws IOException {
        System.out.println("теперь мы в методе sendTExt");

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, text.getBytes().length);
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] resp = "Задача не найдена".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, resp.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(resp);
        }
        exchange.close();
    }
    protected void sendHasInteractions(HttpExchange h) throws IOException {
        byte[] resp = "Задача пересекается по времени".getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected int getPathLength(HttpExchange exchange) {
        String[] pathLength = exchange.getRequestURI().getPath().split("/");
        return pathLength.length;
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange){
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(splitPath[2]));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
 }
