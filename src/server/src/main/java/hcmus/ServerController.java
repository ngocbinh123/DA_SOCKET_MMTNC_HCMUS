package hcmus;



import hcmus.data.Node;
import hcmus.data.NodeFile;
import hcmus.data.Server;
import java.util.ArrayList;
import java.util.List;

public class ServerController extends BaseController<ISocketServerContract.View> implements ISocketServerContract.Controller, Server.ServerListener {
    private List<Node> mNodes = new ArrayList<>();
    private Server mServer;
    @Override
    public void startListenConnections() {
        mServer = new Server(SERVER_PORT, this);
        mServer.run();
    }

    @Override
    public void onHavingNewNode(Node node) {
        mNodes.add(node);
        getView().showNodeOnUI(node);
        for (String name : node.getFileNames()) {
            NodeFile file = new NodeFile(node, name);
            getView().showFileOnUI(file);
        }
    }
}
