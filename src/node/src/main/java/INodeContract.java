import data.Node;
import hcmus.ILifeCycleContract;

public interface INodeContract {
    interface View extends ILifeCycleContract.View {
        void updateDataOnUI(Node node);
        void showMessage(String message);
        void clearInfo();
    }

    interface Controller {
        void requestConnectToServer(String serverIP);
        void disconnect();
    }
}
