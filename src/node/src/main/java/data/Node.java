package data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
    protected Socket currentSocket;

    public Node(String hostName, int ip) {
        PrintWriter out; //  get message from server
        BufferedReader in; // send message to server
        try {
            currentSocket = new Socket(hostName, ip);
            out = new PrintWriter(currentSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            try {
                this.currentSocket = new Socket(hostName, ip);


                String buffer;
                do {
                    buffer = in.readLine();

                    String msg = "";
                    List<String> files = getFiles();
                    for (String file : files) {
                        msg+= file + ";";
                    }
                    out.println(Constant.MSG_WHO_ARE_YOU + ": " + SOCKET_TYPE.NODE.name() + "  - files: " + msg);
                    out.flush();
                }while (buffer == null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        for (int i = 0; i < size + 1; i++) {
            arr.add(String.format("FILE_%d_%d_%d.pdf", currentSocket.getPort(), currentSocket.getLocalPort(), i + 1));
        }

        return arr;
    }
}
