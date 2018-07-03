package hcmus;



import hcmus.data.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerController extends BaseController<ISocketServerContract.View> implements ISocketServerContract.Controller {
    ServerSocket welcomeSocket = null;
    List<Socket> mNodeSockets = new ArrayList<>();

    Map<Socket, List<String>> mNodes= new HashMap();
    Map<Socket, List<String>> mClients= new HashMap();

    Server mServer;
    @Override
    public void startListenConnections() {
        mServer = new Server(SERVER_PORT);
        mServer.run();
    }
}
