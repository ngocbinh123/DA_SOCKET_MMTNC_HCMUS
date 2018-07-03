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
    protected String lastMessage;
    protected PrintWriter out;
    protected BufferedReader in;
    private SOCKET_TYPE type = SOCKET_TYPE.NODE;
    private List<String> files = new ArrayList<>();
    public ServerHandler(Socket client) {
        this.currentSocket = client;
        this.lastMessage = null;
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


                }else if (received.contains(Constant.MSG_PLS_SEND_YOUR_FILES)) {
                    System.out.println(String.format("Socket %d is %s has %d files",currentSocket.getPort(), type.name(), files.size()));
                    received = received.replace(Constant.MSG_PLS_SEND_YOUR_FILES + ":", "").trim();
                    String[] names = received.split(";");
                    for (String name : names) {
                        files.add(name);
                        System.out.println(String.format("%d - %s - %s ", files.size(), type.name(), name));
                    }
                    System.out.println(String.format("----------------------------------------------------------------------"));
                }
            }while (received == null || !received.equalsIgnoreCase(Constant.MSG_QUIT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
