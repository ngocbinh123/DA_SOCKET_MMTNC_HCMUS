package hcmus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
@SuppressWarnings("Duplicates")
public class App
{
    public static void main( String[] args ) {
        System.out.println( "Start Server" );
        ServerFrm.start();
    }

//    public static void main(String[] args) throws IOException {
//        // TODO code application logic here
//        ServerSocket serverSocket = null;
//
//        try {
//            serverSocket = new ServerSocket(10007);
//        }
//        catch (IOException e)
//        {
//            System.err.println("Could not listen on port: 10007.");
//            System.exit(1);
//        }
//
//        Socket clientSocket[] = new Socket[2];
//
//        try {
//            for(byte i = 0; i < clientSocket.length; i ++)
//            {
//                System.out.println ("Waiting for connection from client " + (i +1));
//                clientSocket[i] = serverSocket.accept();
//            }
//        }
//        catch (IOException e)
//        {
//            System.err.println("Accept failed.");
//            System.exit(1);
//        }
//
//        System.out.println ("Connection successful");
//        //System.out.println ("Waiting for input.....");
//
//
//
//        String inputLine;
//        int signal = 0;
//
//        while(true)
//        {
//            String msg = null;
//            for(byte i = 0; i < clientSocket.length; i ++)
//            {
//
//                if(signal == 0)
//                {
//                    sendMessage(clientSocket[i], signal+"");
//                    msg = getMessage(clientSocket[i]);
//                    signal = 1;
//                }
//
//                if(signal == 1)
//                {
//                    sendMessage(clientSocket[(i+1)%2], signal+"");
//                    sendMessage(clientSocket[(i+1)%2], msg);
//                    signal = 0;
//                }
//
//            }
//            if(msg.equals("Bye."))
//                break;
//        }
//
//        for(byte i = 0; i < clientSocket.length; i ++)
//        {
//            clientSocket[i].close();
//        }
//
//        serverSocket.close();
//
//    }
//
//    public static void sendMessage(Socket s, String msg) throws IOException {
//        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
//        out.println(msg);
//    }
//    public static String getMessage(Socket s) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//        return in.readLine();
//    }
}
