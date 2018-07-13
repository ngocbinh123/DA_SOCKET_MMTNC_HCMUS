package data;

import hcmus.BaseInfo;
import hcmus.Constant;
import hcmus.ISocketContract;
import hcmus.SOCKET_TYPE;
import utils.FileUtils;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Node implements ISocketContract {
    private Socket currentSocket;
    private BaseInfo info;
    private NodeListener listener;
    private String hostName;
    private int serverPort;
    BufferedReader in; // receive message from server
    PrintWriter out; // send message to server
    public interface NodeListener {
        void onConnectSuccessful(Node node);
    }

    public Node(int serverPort, NodeListener listener) {
        this(Constant.LOCAL_HOST_NAME, serverPort, listener);
    }

    public Node(String hostName, int port, NodeListener listener) {
        this.hostName = hostName;
        this.serverPort = port;
        this.listener = listener;
    }

    public void reconnect() {
        try {
            currentSocket = new Socket(hostName, serverPort);
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {

    }

    public void connect() {
        try {
            currentSocket = new Socket(hostName, serverPort);
            out = new PrintWriter(currentSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            try {
                String buffer;
                do {
                    buffer = in.readLine();
                    if (buffer == null) {

                    } else if (buffer.contains(Constant.MSG_WHO_ARE_YOU)) {
                        if (info == null) {
                            String sId = buffer.replace(String.format("%s:", Constant.MSG_WHO_ARE_YOU),"").trim();
                            sId = sId.split("-")[0];
                            int id = Integer.parseInt(sId);
                            info = new BaseInfo(id, FileUtils.getFilesByNodeId(id++), SOCKET_TYPE.NODE);
                        }

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
