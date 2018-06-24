package hcmus;

import hcmus.base.BaseController;

public class ClientController extends BaseController<IClientContract.View> implements IClientContract.Controller {
    @Override
    public void requestConnectToServer() {
        Requester client = new Requester();
        client.run();
    }
}
