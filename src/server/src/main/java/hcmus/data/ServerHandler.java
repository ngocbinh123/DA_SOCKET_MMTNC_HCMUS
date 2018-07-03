package hcmus.data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends Thread {
    protected Socket currentSocket;
    protected PrintWriter out;
    protected BufferedReader in;
    private SOCKET_TYPE type = SOCKET_TYPE.NODE;
    private List<String> files = new ArrayList<>();

    private ServerHandleListener listener;
    interface ServerHandleListener {
        void newNode(Socket socket, List<String> files);
    }

    public ServerHandler(Socket client, ServerHandleListener listener) {
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
            this.out.println(Constant.MSG_WHO_ARE_YOU);
            out.flush();

            String received;
            do {
                received = in.readLine();
                if (received == null) {

                }else if (received.contains(Constant.MSG_WHO_ARE_YOU)) {
                    String[] paragraphs = received.split("  - files: ");

                    if (paragraphs[0].contains(SOCKET_TYPE.CLIENT.name())) {
                        received = received.replace(SOCKET_TYPE.CLIENT.name(), "").trim();
                        type = SOCKET_TYPE.CLIENT;
                    }else {
                        type = SOCKET_TYPE.NODE;
                    }

                    if (paragraphs[1].contains(";")) {
                        String[] names = paragraphs[1].trim().split(";");
                        for (String name : names) {
                            if (name != "") {
                                files.add(name);
                                System.out.println(String.format("%d - %s - %s ", files.size(), type.name(), name));
                            }
                        }
                    }
                    listener.newNode(currentSocket, files);
                }
            }while (received == null || !received.equalsIgnoreCase(Constant.MSG_QUIT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
