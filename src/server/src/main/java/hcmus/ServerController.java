package hcmus;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController extends BaseController<ISocketServerContract.View> implements ISocketServerContract.Controller {
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    ServerSocket welcomeSocket = null;

    @Override
    public void startListenConnections() {
        try {
//            1. creating a server socket
            welcomeSocket = new ServerSocket(SERVER_PORT);
            while (true) {
//                2. Wait for connection
                System.out.println("Waiting for connection");
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connection received from " + connectionSocket.getInetAddress().getHostName());

//                3. get Input and Output streams
                out = new ObjectOutputStream(connectionSocket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connectionSocket.getInputStream());
                sendMessage("Connection successful");
                //4. The two parts communicate via the input and output streams
                do{
                    try{
                        message = (String)in.readObject();
                        System.out.println("server>" + message);
                        sendMessage("Hi my server");
                        message = "bye";
                        sendMessage(message);
                    }
                    catch(ClassNotFoundException classNot){
                        System.err.println("data received in unknown format");
                    }
                }while(!message.equals("bye"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                welcomeSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendMessage(String msg) {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
