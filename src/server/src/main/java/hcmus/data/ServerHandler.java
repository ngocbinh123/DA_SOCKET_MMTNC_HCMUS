package hcmus.data;

import hcmus.BaseInfo;
import hcmus.Constant;
import hcmus.SOCKET_TYPE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler extends Thread {
    private BaseInfo info;
    private int newNodeId;
    private int newClientId;
    protected Socket currentSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    private SOCKET_TYPE type = SOCKET_TYPE.NODE;

    private ServerHandleListener listener;
    interface ServerHandleListener {
        void newNode(Node node);
        void newClient(Client client);
        void closeSocket(SOCKET_TYPE type, int id);
    }

    public ServerHandler(Socket client, ServerHandleListener listener, int newNodeId, int newClientId) {
        this.newNodeId = newNodeId;
        this.newClientId = newClientId;
        this.currentSocket = client;
        this.listener = listener;
        this.start();
    }

    public void run() {
        System.out.println("New Communication Thread Started");
        try {
//            to use send message to client
            this.out = new PrintWriter(currentSocket.getOutputStream(), true);
//            to use get message from client
            this.in = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));
            this.out.println(String.format("%s:%d-%d", Constant.MSG_WHO_ARE_YOU, newNodeId, newClientId));
            out.flush();

            String received;
            do {
                if (!currentSocket.isConnected()) {
                    return;
                }
                received = in.readLine();
                if (received != null) {
                    System.out.println("===> " + received);
                }

                if (received == null) {

                } else if (received.equalsIgnoreCase(Constant.MSG_QUIT)) {
                    closeHandle();
                } else if (received.contains(Constant.MSG_WHO_ARE_YOU)) {
                    String json = received.replace(Constant.MSG_WHO_ARE_YOU + ": ", "");
                    info = BaseInfo.parseToObject(json);
                    this.type = info.getType();
                    this.newNodeId = info.getId();
                    if (type == SOCKET_TYPE.NODE) {
                        listener.newNode(new Node(newNodeId, info.getName(), currentSocket, info.getFileNames()));
                    }else {
                        Client client = new Client(newNodeId, info.getName(), currentSocket);
                        listener.newClient(client);
                    }
                }
            }while (received == null || !received.equalsIgnoreCase(Constant.MSG_QUIT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeHandle() {
        try {
            out.close();
            in.close();
            currentSocket.close();
            listener.closeSocket(type, type == SOCKET_TYPE.NODE ? newNodeId : newClientId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.out.println(Constant.SERVER_IS_CLOSE);
        out.flush();

//        closeHandle();
    }
}
