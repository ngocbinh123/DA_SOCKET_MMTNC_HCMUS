package hcmus;

import hcmus.RUDP.UDPReceiver;
import hcmus.RUDP.UDPSender;
import hcmus.data.Client;
import hcmus.data.NodeFile;

import java.awt.*;
import java.io.File;
import java.util.List;

import static hcmus.Constant.LOCAL_HOST_NAME;

public class ClientController extends BaseController<IClientContract.View> implements IClientContract.Controller, Client.ClientListener {
    private Client client;
    @Override
    public void requestConnectToServer() {
        if (client == null) {
            client = new Client(LOCAL_HOST_NAME, SERVER_PORT, this);
            client.connect();
        }else {
            client.reconnect();
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void requestDownload(int index, NodeFile nodeFile) {
        new Thread(()-> {
            UDPReceiver receiver = new UDPReceiver(nodeFile.getNodePort());
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
        EventQueue.invokeLater(() -> client.receiveFilesFromServer());
    }

    @Override
    public void receiveFilesFromServer(List<NodeFile> files) {
        getView().showFilesOnUI(files);
    }

}
