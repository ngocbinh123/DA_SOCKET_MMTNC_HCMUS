package hcmus.data;

import hcmus.SOCKET_TYPE;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements ServerHandler.ServerHandleListener {
    private ServerSocket server;
    private List<Node> mStoredNodes = new ArrayList<>();
    private List<Client> mStoredClients = new ArrayList<>();

    private ServerListener listener;
    public interface ServerListener {
        void onHavingNewNode(Node node);
        void onHavingNewClient(Client client);
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

    private int mNodesIndex = 1;
    private int mClientsIndex = 1;
    public void run() {
        while (true) {
            try {
                Socket newSocket = server.accept();
                System.out.println(String.format("New client has port is %d connected.", newSocket.getPort()));
                new ServerHandler(newSocket, this, mNodesIndex, mClientsIndex);
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
    public void newNode(Node node) {
        mNodesIndex++;
        mStoredNodes.add(node);
        listener.onHavingNewNode(node);
    }

    @Override
    public void newClient(final Client client) {
        mClientsIndex++;
        mStoredClients.add(client);
        listener.onHavingNewClient(client);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SendFileToClient(client, mStoredNodes);
            }
        });
    }

    @Override
    public void closeSocket(SOCKET_TYPE type, int id) {
        switch (type) {
            case NODE:
                for (Node node : mStoredNodes) {
                    if (node.getId() == id) {
                        mStoredNodes.remove(node);
                        listener.nodeIsClosed(node);
                        break;
                    }
                }
                break;
            case CLIENT:
                for (Client client : mStoredClients) {
                    if (client.getId() == id) {
                        mStoredClients.remove(client);
                        listener.clientIsClosed(client);
                        break;
                    }
                }
                break;
        }
    }
}
