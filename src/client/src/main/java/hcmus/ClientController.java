package hcmus;

import hcmus.data.Client;

import static hcmus.Constant.LOCAL_HOST_NAME;

public class ClientController extends BaseController<IClientContract.View> implements IClientContract.Controller, Client.ClientListener {
    private Client client;
    @Override
    public void requestConnectToServer() {
        if (client == null) {
            client = new Client(LOCAL_HOST_NAME, SERVER_PORT, this);
            client.connect();
        }else {
            client.reconnect();
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onConnectSuccessful(Client client) {
        getView().updateDataOnUI(client);
    }
}
