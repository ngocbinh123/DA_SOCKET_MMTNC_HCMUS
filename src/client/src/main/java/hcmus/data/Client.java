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
    }

    public Client(String hostName, int ip, ClientListener listener) {
        this.hostName = hostName;
        this.ip = ip;
        this.listener = listener;
    }

    @Override
    public void connect() {
        try {
            currentSocket = new Socket(hostName, ip);
            out = new PrintWriter(currentSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            try {
                String buffer;
                do {
                    buffer = in.readLine();
                    if (buffer != null && buffer.contains(Constant.MSG_WHO_ARE_YOU)) {
                        if (info == null) {
                            String sId = buffer.replace(String.format("%s:", Constant.MSG_WHO_ARE_YOU),"").trim();
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
        } catch (IOException e) {
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
