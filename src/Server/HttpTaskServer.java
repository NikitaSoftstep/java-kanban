package Server;

import Server.Handlers.*;
import com.sun.net.httpserver.HttpServer;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    protected static final int PORT = 8080;
    private TaskManager manager;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public HttpServer createServer() {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            createServerHandlers(httpServer);
            return httpServer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void createServerHandlers(HttpServer incomingServer) {
        incomingServer.createContext("/tasks", new TasksHandler(manager));
        incomingServer.createContext("/subtasks", new SubtasksHandler(manager));
        incomingServer.createContext("/epics", new EpicsHandler(manager));
        incomingServer.createContext("/history", new HistoryHandler(manager));
        incomingServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }
}
