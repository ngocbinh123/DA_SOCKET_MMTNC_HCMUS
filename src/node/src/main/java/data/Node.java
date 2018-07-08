package data;

import hcmus.BaseInfo;
import hcmus.Constant;
import hcmus.SOCKET_TYPE;
import utils.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class Node {
    private Socket currentSocket;
    private BaseInfo info;
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
                        int id = Integer.parseInt(sId);
                        info = new BaseInfo(id, FileUtils.getFilesByNodeId(id++), SOCKET_TYPE.NODE);
                        out.println(Constant.MSG_WHO_ARE_YOU + ": " + info.parseToString());
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
    
    public String getName() {
        return info.getName();
    }

    public List<File> getFiles() {
        return info.getFiles();
    }

    public int getLocalPort() {
        return currentSocket.getLocalPort();
    }

    public int getFileSize() {
        return info.getFiles().size();
    }

    public void close() {
        out.println(Constant.MSG_QUIT);
        out.flush();
    }
}
