package hcmus;

import hcmus.data.Client;

public interface IClientContract {
    interface View extends ILifeCycleContract.View {
        void updateDataOnUI(Client client);
    }

    interface Controller {
        void requestConnectToServer();
        void disconnect();
    }
}
