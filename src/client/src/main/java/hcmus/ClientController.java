package hcmus;

import hcmus.RUDP.Receiver;
import hcmus.RUDP.UDPReceiver;
import hcmus.RUDP.UDPSender;
import hcmus.data.Client;
import hcmus.data.NodeFile;

import java.io.File;
import java.util.List;

public class ClientController extends BaseController<IClientContract.View> implements IClientContract.Controller, Client.ClientListener {
    private Client client;
    @Override
    public void requestConnectToServer() {
        if (client == null) {
            client = new Client(SERVER_PORT, this);
            client.connect();
        }else {
            client.reconnect();
        }
    }

    @Override
    public void disconnect() {
        client.disconnect();
    }

    @Override
    public void requestNodeSendFileByUDPReliable(int index, NodeFile nodeFile, String storagePath) {
        new Thread(()-> new Receiver(nodeFile.getNodePort(), new Receiver.HandleListener() {
            @Override
            public void onCompleted(File file) {
                nodeFile.setLocalFile(file);
                getView().downloadSuccessful(index, nodeFile);
            }

            @Override
            public void onError(String err) {

            }
        })).start();

        new Thread(() -> {
            UDPSender sender = new UDPSender(nodeFile.getNodePort()+1, Constant.LOCAL_HOST_NAME);
            sender.sendMessage(String.format("%s:%s;%s",Constant.MSG_PLS_SEND_YOUR_FILES, nodeFile.getName(), storagePath));
        }).start();
    }

    @Override
    public void requestDownload(int index, NodeFile nodeFile, String storagePath) {
        new Thread(()-> {
            UDPReceiver receiver = new UDPReceiver(nodeFile.getNodePort(), storagePath);
            receiver.startListen(new UDPReceiver.ReceiveListener() {
                @Override
                public void onReceived(File file) {
                    nodeFile.setLocalFile(file);
                    getView().downloadSuccessful(index, nodeFile);
                }

                @Override
                public void failure(String err) {
                    getView().downloadFailure(err);
                }
            });
        }).start();

        new Thread(() -> {
            UDPSender sender = new UDPSender(nodeFile.getNodePort()+1, Constant.LOCAL_HOST_NAME);
            sender.sendMessage(String.format("%s:%s",Constant.MSG_PLS_SEND_YOUR_FILES, nodeFile.getName()));
        }).start();
    }

    @Override
    public void onConnectSuccessful(Client client) {
        getView().updateDataOnUI(client);
        client.startListenFromServer();
    }

    @Override
    public void receiveFilesFromServer(List<NodeFile> files) {
        getView().showFilesOnUI(files);
    }

    @Override
    public void removeFilesByNodeId(String nodeId) {
        getView().removeFilesByNodeId(nodeId);
    }
}
