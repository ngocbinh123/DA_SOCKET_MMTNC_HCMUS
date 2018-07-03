package hcmus;



import hcmus.data.Server;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerController extends BaseController<ISocketServerContract.View> implements ISocketServerContract.Controller, Server.ServerListener {
    Map<Socket, List<String>> mNodes= new HashMap();
    Map<Socket, List<String>> mClients= new HashMap();

    Server mServer;
    @Override
    public void startListenConnections() {
        mServer = new Server(SERVER_PORT, this);
        mServer.run();
    }

    @Override
    public void newNode(int id, String name, List<String> files) {
    }
}
