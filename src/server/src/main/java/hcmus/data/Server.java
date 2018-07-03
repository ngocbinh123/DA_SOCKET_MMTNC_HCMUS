package hcmus.data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket server;
    private List<Socket> nodes = new ArrayList<>();
    private List<Socket> clients = new ArrayList<>();
    public Server(int port) {
        try {
            this.server = new ServerSocket(port);
            System.out.println("New server initialized!");
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket newSocket = server.accept();
                System.out.println(String.format("New client has port is %d connected.", newSocket.getPort()));
                new ServerHandler(newSocket);
                System.out.println("New server initialized!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void close() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
