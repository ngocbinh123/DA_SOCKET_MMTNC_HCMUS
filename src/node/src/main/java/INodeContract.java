import data.Node;
import hcmus.ILifeCycleContract;

public interface INodeContract {
    interface View extends ILifeCycleContract.View {
        void updateDataOnUI(boolean isConnect, Node node);
    }

    interface Controller {
        void requestConnectToServer();
        void disconnect();
    }
}
