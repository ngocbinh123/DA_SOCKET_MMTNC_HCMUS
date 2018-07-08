package hcmus.data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements ServerHandler.ServerHandleListener {
    private ServerSocket server;
    private List<Node> nodes = new ArrayList<>();
    private List<Socket> clients = new ArrayList<>();

    private ServerListener listener;
    public interface ServerListener {
        void onHavingNewNode(Node node);
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

    @Override
    public synchronized void start() {
        super.start();
        run();
    }

    public void run() {
        while (true) {
            try {
                Socket newSocket = server.accept();
                System.out.println(String.format("New client has port is %d connected.", newSocket.getPort()));
                new ServerHandler(newSocket, this, nodes.size() + 1);
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
        int id = nodes.size() + 1;
        String name = String.format("NODE_%d_%d", id, files.size());
        Node newNode = new Node(id, name, socket, files);
        nodes.add(newNode);
        listener.onHavingNewNode(newNode);
    }
}
