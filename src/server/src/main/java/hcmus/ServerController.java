package hcmus;



import hcmus.data.Client;
import hcmus.data.Node;
import hcmus.data.NodeFile;
import hcmus.data.Server;

import java.util.ArrayList;

public class ServerController extends BaseController<ISocketServerContract.View> implements ISocketServerContract.Controller, Server.ServerListener {
    private Server mServer;
    @Override
    public void startListenConnections() {
        mServer = new Server(SERVER_PORT, this);
        mServer.run();
    }

    @Override
    public void stopListen() {
        mServer.close();
    }

    @Override
    public void onHavingNewNode(Node node) {
        getView().showNodeOnUI(node);
        ArrayList<NodeFile> ls = new ArrayList<>();
        for (String name : node.getFileNames()) {
            NodeFile file = new NodeFile(node, name);
            ls.add(file);
        }
        getView().showFilesOnUI(ls);
    }

    @Override
    public void onHavingNewClient(Client client) {
        getView().showClientOnUI(client);
    }

    @Override
    public void nodeIsClosed(Node node) {
        getView().closeNode(node);
    }

    @Override
    public void clientIsClosed(Client client) {
        getView().closeClient(client);
    }
}
