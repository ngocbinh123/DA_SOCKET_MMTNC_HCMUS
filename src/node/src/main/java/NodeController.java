import data.Node;
import hcmus.BaseController;
import hcmus.Constant;
import hcmus.RUDP.Sender;
import hcmus.RUDP.UDPReceiver;
import hcmus.RUDP.UDPSender;

import java.io.File;

public class NodeController extends BaseController<INodeContract.View> implements INodeContract.Controller, Node.NodeListener {
    private Node node;
    public void requestConnectToServer() {
        if (node == null) {
            node = new Node(SERVER_PORT, this);
            node.connect();
        }else {
            node.reconnect();
        }
    }

    @Override
    public void onConnectSuccessful(Node node) {
        getView().updateDataOnUI(node);
        listenRequestFromClient();
    }

    @Override
    public void disconnect() {
        node.close();
    }

    private void listenRequestFromClient() {
        new Thread(() -> {
            System.out.println(String.format("node listen on port %d", node.getLocalPort()));
            UDPReceiver receiver = new UDPReceiver(node.getLocalPort()+1);
            receiver.listeningMessage(new UDPReceiver.ReceiveListenMessage() {
                @Override
                public void onReceived(String message) {
                    System.out.println("onReceived: " + message);
                    if (message.contains(Constant.MSG_PLS_SEND_YOUR_FILES)) {
                        message = message.replace(Constant.MSG_PLS_SEND_YOUR_FILES+":","");
                        String[] parts = message.split(";");
                        for (File f:node.getFiles()) {
                            if (message.contains(f.getName())) {
//                                UDPSender sender = new UDPSender(node.getLocalPort(), Constant.LOCAL_HOST_NAME);
//                                sender.sendFile(f);
                                String storagePath = parts[1].split(" ")[0].trim();
                                new Sender(node.getLocalPort(), f, storagePath);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onError(String err) {
                    System.out.println("listenRequestFromClient: has error: " + err);
                }
            });
        }).start();
    }
}
