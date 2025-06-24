package com.plexushr.server;
 
import com.plexushr.controller.EmployeeController;
import com.sun.net.httpserver.HttpServer;
 
import java.io.IOException;
import java.net.InetSocketAddress;
 
public class SimpleHttpServer {
 
    private static final int PORT = 8000; // Backend server port
 
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
 
        // Contexts for API endpoints
        server.createContext("/api/", EmployeeController::handleRequest);
 
        server.setExecutor(null); // Creates a default executor
        server.start();
 
        System.out.println("Plexus HR Backend Server started on port " + PORT);
        System.out.println("Access frontend at http://localhost:5500 (or your Live Server port)");
        System.out.println("API Endpoints available at http://localhost:" + PORT + "/api/...");
    }
}
 