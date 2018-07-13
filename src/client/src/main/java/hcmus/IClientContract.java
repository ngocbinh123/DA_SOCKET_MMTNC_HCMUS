package hcmus;

import hcmus.data.Client;
import hcmus.data.NodeFile;

import java.util.List;

public interface IClientContract {
    interface View extends ILifeCycleContract.View {
        void updateDataOnUI(Client client);
        void showFilesOnUI(List<NodeFile> ls);
        void downloadFailure(String err);
        void downloadSuccessful(int index, NodeFile file);
    }

    interface Controller {
        void requestConnectToServer();
        void disconnect();
        void requestDownload(int index, NodeFile file, String storageDir);
    }
}
