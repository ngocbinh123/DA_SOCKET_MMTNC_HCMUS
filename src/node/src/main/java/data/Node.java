package data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;
import utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Node {
    private Socket currentSocket;
    private int id;
    private String name;
    private List<File> files = new ArrayList<>();
    private NodeListener listener;
    BufferedReader in; // send message to server
    PrintWriter out;
    public interface NodeListener {
        void onConnectSuccessful(Node node);
    }
    public Node(String hostName, int ip, NodeListener listener) {
        this.listener = listener;
        try {
            currentSocket = new Socket(hostName, ip);
            out = new PrintWriter(currentSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            try {
                String buffer;
                do {
                    buffer = in.readLine();
                    if (buffer != null && buffer.contains(Constant.MSG_WHO_ARE_YOU)) {
                        String sId = buffer.replace(String.format("%s:", Constant.MSG_WHO_ARE_YOU),"").trim();
                        this.id = Integer.parseInt(sId);
                        List<String> files = getFiles(id);
                        this.name = String.format("NODE_%d_%d", id, files.size());
                        StringBuilder msg = new StringBuilder();
                        for (String file : files) {
                            msg.append(file).append(";");
                        }
                        out.println(Constant.MSG_WHO_ARE_YOU + ": " + SOCKET_TYPE.NODE.name() + "  - files: " + msg);
                        out.flush();
                        listener.onConnectSuccessful(this);
                    }
                }while (buffer == null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printOnConsole(String msg, SOCKET_TYPE type) {
        System.out.println(String.format("%s: %s", type.name(), msg));
    }

    private List<String> getFiles(int id) {
        if (files.isEmpty()) {
            files.addAll(FileUtils.getFilesByNodeId(id++));
        }

        List<String> arr = new ArrayList<>();
        for (File file : files) {
            arr.add(file.getName());
        }
        return arr;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<File> getFiles() {
        return files;
    }

    public int getLocalPort() {
        return currentSocket.getLocalPort();
    }

    public int getFileSize() {
        return files.size();
    }

    public void close() {
        try {
            out.println(Constant.MSG_QUIT);
            out.flush();
            TimeUnit.SECONDS.sleep(5);
            currentSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
