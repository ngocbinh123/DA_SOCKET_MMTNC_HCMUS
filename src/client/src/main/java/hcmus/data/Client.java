package hcmus.data;

import hcmus.BaseInfo;
import hcmus.Constant;
import hcmus.ISocketContract;
import hcmus.SOCKET_TYPE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements ISocketContract {
    private Socket currentSocket;
    private String hostName;
    private int ip;
    private ClientListener listener;
    private BaseInfo info;

    BufferedReader in; // receive message from server
    PrintWriter out; // send message to server

    public interface ClientListener {
        void onConnectSuccessful(Client client);
        void receiveFilesFromServer(List<NodeFile> files);
    }

    public Client(String hostName, int ip, ClientListener listener) {
        this.hostName = hostName;
        this.ip = ip;
        this.listener = listener;
    }

    private List<NodeFile> files = new ArrayList<>();
    private List<String> nodesJson = new ArrayList<>();

    @Override
    public void connect() {
        try {
            currentSocket = new Socket(hostName, ip);
            out = new PrintWriter(currentSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            startListen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListen() {
        new Thread(()-> {
            try {
                String buffer = null;
                do {
                    String message = in.readLine();
                    buffer = message;
                    if (message != null && message.contains(Constant.MSG_WHO_ARE_YOU)) {
                        if (info == null) {
                            String sId = message.replace(String.format("%s:", Constant.MSG_WHO_ARE_YOU),"").trim();
                            sId = sId.split("-")[1];
                            int id = Integer.parseInt(sId);
                            info = new BaseInfo(id, SOCKET_TYPE.CLIENT);
                        }
                        out.println(Constant.MSG_WHO_ARE_YOU + ": " + info.parseToString());
                        out.flush();
                        listener.onConnectSuccessful(this);
                    }
                }while (buffer == null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void receiveFilesFromServer() {
        try {
            boolean isCompleted = false;
            do {
                String message = in.readLine();
                if (message.equalsIgnoreCase(Constant.MSG_COMPLETED)) {
                    isCompleted = true;
                }else if (message != null && message.contains("{") && message.contains("}")) {
                    message = message.replace("{", "");
                    message = message.replace("}", "");
                    nodesJson.add(message);
                }

                if (nodesJson.size() > 0 && isCompleted) {
                    for (String json : nodesJson) {
                        String[] pars = json.split(",");
                        String id= pars[0].replace("id:","").trim();
                        String ip= pars[1].replace("ip:","").trim();
                        String port= pars[2].replace("port:","").trim();
                        String localPort= pars[3].replace("localPort:","").trim();
                        String fileNamesJson= pars[4].replace("files:","").trim();
                        List<String> fileNames = BaseInfo.getFilesFromJson(fileNamesJson);

                        for (String name: fileNames) {
                            NodeFile f = new NodeFile(name, id, ip, port, localPort);
                            files.add(f);
                        }
                    }
                    listener.receiveFilesFromServer(files);
                  }
            }while (!isCompleted);
//            while (true) {
//                message = in.readLine();
//                if (message != null && message.contains("{") && message.contains("}")) {
//                    message = message.replace("{", "");
//                    message = message.replace("}", "");
//                    nodesJson.add(message);
//                }else if (nodesJson.size() > 0 && message == null) {
//                    for (String json : nodesJson) {
//                        String[] pars = json.split(",");
//                        String id= pars[0].replace("id:","").trim();
//                        String ip= pars[1].replace("ip:","").trim();
//                        String port= pars[2].replace("port:","").trim();
//                        String localPort= pars[3].replace("localPort:","").trim();
//                        String fileNamesJson= pars[4].replace("files:","").trim();
//                        List<String> fileNames = BaseInfo.getFilesFromJson(fileNamesJson);
//
//                        for (String name: fileNames) {
//                            NodeFile f = new NodeFile(name, id, ip, port, localPort);
//                            files.add(f);
//                        }
//                    }
//                    listener.receiveFilesFromServer(files);
//                    return;
//                }
//            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reconnect() {
    }

    @Override
    public void disconnect() {

    }

    public String getName() {
        return info.getName();
    }

    public int getLocalPort() {
        return currentSocket.getLocalPort();
    }
}
