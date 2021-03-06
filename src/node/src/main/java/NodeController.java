import data.Node;
import hcmus.BaseController;
import hcmus.Constant;
import hcmus.RUDP.UDPReceiver;
import hcmus.RUDP.UDPSender;

import java.io.File;
import static hcmus.Constant.FOLDER_RECEICE;

public class NodeController extends BaseController<INodeContract.View> implements INodeContract.Controller, Node.NodeListener {
    private Node node;
    public void requestConnectToServer() {
        if (node == null) {
            node = new Node("localhost", SERVER_PORT, this);
        }else {
            node.reconnect();
        }
    }

    @Override
    public void onConnectSuccessful(Node node) {
        getView().updateDataOnUI(true, node);
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
                        for (File f:node.getFiles()) {
                            if (f.getName().equals(message)) {
                                UDPSender sender = new UDPSender(node.getLocalPort(), Constant.LOCAL_HOST_NAME);
                                sender.sendFile(f, FOLDER_RECEICE);
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
