package hcmus;

import hcmus.data.Client;
import hcmus.data.NodeFile;

import java.util.List;

public interface IClientContract {
    interface View extends ILifeCycleContract.View {
        void updateDataOnUI(Client client);
        void showFilesOnUI(List<NodeFile> ls);
        void removeFilesByNodeId(String nodeId);
        void downloadFailure(String err);
        void downloadSuccessful(int index, NodeFile file);
        void showMessage(String message);
    }

    interface Controller {
        void requestConnectToServer(String serverIP);
        void disconnect();
        void requestDownload(int index, NodeFile nodeFile, String storagePath);
        void requestNodeSendFileByUDPReliable(int index, NodeFile nodeFile, String storagePath);
    }
}
