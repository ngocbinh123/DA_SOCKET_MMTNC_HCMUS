package data;


import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeHandler {
    protected Socket currentSocket;
    protected PrintWriter out;
    private BufferedReader in;

    public NodeHandler(Socket socket) {
        this.currentSocket = socket;

        try {
            this.out = new PrintWriter(currentSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String buffer;
            do {
                buffer = in.readLine();
                if (buffer != null) {
                    String msg = "";
                    List<String> files = getFiles();
                    for (String file : files) {
                        msg+= file + ";";
                    }
                    out.println(Constant.MSG_WHO_ARE_YOU + ": " + SOCKET_TYPE.NODE.name() + "  - files: " + msg);
                    out.flush();
                }
            }while (!buffer.equalsIgnoreCase(Constant.MSG_QUIT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printOnConsole(String msg, SOCKET_TYPE type) {
        System.out.println(String.format("%s: %s", type.name(), msg));
    }

    private List<String> getFiles() {
        List<String> arr = new ArrayList<String>();
        int size = new Random().nextInt(10);
        for (int i = 1; i < size; i++) {
            arr.add(String.format("FILE_%d_%d_%d.pdf", currentSocket.getPort(), currentSocket.getLocalPort(), i));
        }

        return arr;
    }
}
