package hcmus;

import hcmus.data.Node;
import hcmus.data.NodeFile;

public interface ISocketServerContract {
    interface View extends ILifeCycleContract.View {
        void showFileOnUI(NodeFile file);
        void showNodeOnUI(Node node);
    }

    interface Controller {
        void startListenConnections();
    }
}
