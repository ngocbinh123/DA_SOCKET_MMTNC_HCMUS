package hcmus.data;

import hcmus.SOCKET_TYPE;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server extends Thread implements ServerHandler.ServerHandleListener {
    private ServerSocket server;

    private List<ServerHandler> mStoredHandler = new ArrayList<>();
    private List<Node> mStoredNodes = new ArrayList<>();
    private List<Client> mStoredClients = new ArrayList<>();
    private int mNodesIndex = 1;
    private int mClientsIndex = 1;
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
                ServerHandler handler = new ServerHandler(newSocket, this, mNodesIndex, mClientsIndex);
                mStoredHandler.add(handler);
                System.out.println("New server initialized!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            for (ServerHandler handler : mStoredHandler) {
                handler.close();
            }
            mStoredClients.clear();
            mStoredNodes.clear();
            mStoredHandler.clear();
            server.close();
//            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void newNode(Node node) {
        mNodesIndex++;
        mStoredNodes.add(node);
        listener.onHavingNewNode(node);
        sendFilesOfNewNodeToClient(node);
    }

    private void sendFilesOfNewNodeToClient(Node node) {
        if (!mStoredClients.isEmpty()) {
            new Thread(() -> {
                for (Client client : mStoredClients) {
                    new SendFileToClient(client, Arrays.asList(node));
                }
            }).start();
        }
    }

    @Override
    public void newClient(final Client client) {
        mClientsIndex++;
        mStoredClients.add(client);
        listener.onHavingNewClient(client);
        sendFileToClients(client);
    }

    private void sendFileToClients(Client client) {
        new Thread(() -> new SendFileToClient(client, mStoredNodes) ).start();
    }

    @Override
    public void closeSocket(SOCKET_TYPE type, int id) {
        switch (type) {
            case NODE:
                notifyClientToRemoveFilesOfClosedNode(id);
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

    private void notifyClientToRemoveFilesOfClosedNode(int nodeId) {
        new Thread(() -> {
            for (Client client : mStoredClients) {
                SendDataToClient.pleaseRemoveFileByNodeId(client, nodeId);
            }
        }).start();
    }
}
