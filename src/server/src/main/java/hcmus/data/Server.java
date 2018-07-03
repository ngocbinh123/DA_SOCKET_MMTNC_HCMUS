package hcmus.data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server extends Thread implements ServerHandler.ServerHandleListener {
    private ServerSocket server;
    private List<Socket> nodes = new ArrayList<>();
    private List<Socket> clients = new ArrayList<>();

    private Map<Integer, List<String>> mapFiles = new HashMap<>();
    private ServerListener listener;
    public interface ServerListener {
        void newNode(int id, String name, List<String> files);
    }

    public Server(int port, ServerListener listener) {
        this.listener = listener;
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
                new ServerHandler(newSocket, this);
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

    @Override
    public void newNode(Socket socket, List<String> files) {
        nodes.add(socket);
        int id = nodes.size();
        mapFiles.put(id, files);
        listener.newNode(id, String.format("NODE_%d_%d", id, files.size()), files);
    }
}
