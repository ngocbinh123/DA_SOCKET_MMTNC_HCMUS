package data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;
import rx.Observable;
import rx.Scheduler;
import utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Socket currentSocket;
    private int id;
    private String name;
    private List<File> files = new ArrayList<>();
    private NodeListener listener;

    public interface NodeListener {
        void onConnectSuccessful(Node node);
    }
    public Node(String hostName, int ip, NodeListener listener) {
        this.listener = listener;
        PrintWriter out; //  get message from server
        BufferedReader in; // send message to server
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
                        String msg = "";
                        for (String file : files) {
                            msg+= file + ";";
                        }
                        out.println(Constant.MSG_WHO_ARE_YOU + ": " + SOCKET_TYPE.NODE.name() + "  - files: " + msg);
                        out.flush();
                        listener.onConnectSuccessful(this);
                    }
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

    private List<String> getFiles(int id) {
        if (files.isEmpty()) {
            files.addAll(FileUtils.getFilesByNodeId(id++));
        }

        List<String> arr = new ArrayList<String>();
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
            PrintWriter out = new PrintWriter(currentSocket.getOutputStream());
            out.println(Constant.MSG_QUIT);
            currentSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
