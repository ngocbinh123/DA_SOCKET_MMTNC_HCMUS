package hcmus.data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.IOException;
import java.io.PrintWriter;

public class SendDataToClient {
    public static void pleaseRemoveFileByNodeId(Client client, int nodeId) {
        try {
            PrintWriter out = new PrintWriter(client.getSocket().getOutputStream(), true);
            out.println(String.format(Constant.MSG_NOTIFY_SOCKET_IS_QUIT, SOCKET_TYPE.NODE.name(), nodeId));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
