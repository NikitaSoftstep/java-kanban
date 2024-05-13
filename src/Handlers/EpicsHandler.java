package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.FileBackedTaskManager;
import taskmanager.TaskManager;

import java.io.IOException;

public class EpicsHandler implements HttpHandler {

    TaskManager manager;
    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
