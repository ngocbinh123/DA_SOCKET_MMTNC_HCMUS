package hcmus.data;

import hcmus.BaseInfo;
import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerHandler extends Thread {
    private BaseInfo info;
    private int newId;
    protected Socket currentSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    private SOCKET_TYPE type = SOCKET_TYPE.NODE;

    private ServerHandleListener listener;
    interface ServerHandleListener {
        void newNode(int id, Socket socket, List<String> files);
        void closeSocket(SOCKET_TYPE type, int id);
        void newClient(Socket socket);
    }

    public ServerHandler(Socket client, ServerHandleListener listener, int newId) {
        this.newId = newId;
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
            this.out.println(String.format("%s:%d", Constant.MSG_WHO_ARE_YOU, newId));
            out.flush();

            String received;
            do {
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
                    this.newId = info.getId();
                    listener.newNode(newId, currentSocket, info.getFileNames());
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
            listener.closeSocket(type, newId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
