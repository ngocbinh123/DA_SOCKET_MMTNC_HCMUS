package hcmus.data;

import hcmus.Constant;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SendFileToClient extends Thread {
    private Client client;
    private List<Node> nodes;

    protected PrintWriter out;
//    protected BufferedReader in;

    public SendFileToClient(Client client, List<Node> nodes) {
        this.client = client;
        this.nodes = nodes;
        this.start();
    }

    @Override
    public synchronized void start() {
        super.start();
        sendFilesToClient();
    }

    public void sendFilesToClient() {
        try {
            this.out = new PrintWriter(client.getSocket().getOutputStream(), true);
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                String ip = node.getSocket().getRemoteSocketAddress().toString();
                ip = ip.replaceAll("\\\\","\\\\\\\\");
                ip = ip.replace(String.format(":%d",node.getSocket().getLocalPort()),"");
                String nodeJson = String.format("{id:%d,ip:%s,port:%d,localPort:%d,files:%s}",
                        node.getId(),
                        ip,
                        node.getSocket().getPort(),
                        node.getSocket().getLocalPort(),
                        node.getFileNamesJson()
                );
                out.println(nodeJson);
                out.flush();
            }
            out.println(Constant.MSG_COMPLETED);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
