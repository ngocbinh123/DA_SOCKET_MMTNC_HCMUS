import data.Node;
import hcmus.BaseController;

public class NodeController extends BaseController<INodeContract.View> implements INodeContract.Controller, Node.NodeListener {
    private Node node;
    public void requestConnectToServer() {
        node = new Node("localhost", SERVER_PORT, this);
    }

    @Override
    public void onConnectSuccessful(Node node) {
        getView().updateDataOnUI(true, node);
    }

    @Override
    public void disconnect() {
        node.close();
    }
}
