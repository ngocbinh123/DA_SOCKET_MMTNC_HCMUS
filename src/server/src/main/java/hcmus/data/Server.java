package hcmus.data;

import hcmus.SOCKET_TYPE;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements ServerHandler.ServerHandleListener {
    private ServerSocket server;
    private List<Node> nodes = new ArrayList<>();
    private List<Socket> clients = new ArrayList<>();
    private List<Node> disNodes = new ArrayList<>();
    private List<Socket> disClients = new ArrayList<>();

    private ServerListener listener;
    public interface ServerListener {
        void onHavingNewNode(Node node);
        void nodeIsClosed(Node node);
        void clientIsClosed(Client client);
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

    private int count = 1;
    public void run() {
        while (true) {
            try {
                Socket newSocket = server.accept();
                System.out.println(String.format("New client has port is %d connected.", newSocket.getPort()));
                new ServerHandler(newSocket, this, count++);
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
    public void newNode(int id, Socket socket, List<String> files) {
        String name = String.format("NODE_%d_%d", id, files.size());
        Node newNode = new Node(id, name, socket, files);
        nodes.add(newNode);
        listener.onHavingNewNode(newNode);
    }

    @Override
    public void closeSocket(SOCKET_TYPE type, int id) {
        switch (type) {
            case NODE:
                for (Node node : nodes) {
                    if (node.getId() == id) {
                        nodes.remove(node);
                        disNodes.add(node);
                        listener.nodeIsClosed(node);
                        break;
                    }
                }
                break;
            case CLIENT:

                break;
        }
    }

    @Override
    public void newClient(Socket socket) {

    }
}
