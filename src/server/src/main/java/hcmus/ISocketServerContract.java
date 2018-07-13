package hcmus;

import hcmus.data.Client;
import hcmus.data.Node;
import hcmus.data.NodeFile;

import java.util.ArrayList;

public interface ISocketServerContract {
    interface View extends ILifeCycleContract.View {
        void showFileOnUI(NodeFile file);
        void showFilesOnUI(ArrayList<NodeFile> files);
        void showNodeOnUI(Node node);
        void showClientOnUI(Client client);
        void closeNode(Node node);
        void closeClient(Client client);
    }

    interface Controller {
        void startListenConnections();
    }
}
