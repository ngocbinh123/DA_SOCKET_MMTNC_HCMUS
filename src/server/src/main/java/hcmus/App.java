package hcmus;

import javax.swing.*;
import java.io.*;
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
//        Provider server = new Provider();
//        while(true){
//            server.run();
//        }

        ServerFrm.start();
//        String clientSentence;
//        String capitalizedSentence;
//        ServerSocket welcomeSocket;
//        ObjectOutputStream out;
//        ObjectInputStream in;
//
//        try {
//            welcomeSocket = new ServerSocket(6789);
//            Socket connectionSocket = welcomeSocket.accept();
//            System.out.println("Waiting for connection");
//
//            while (true) {
//                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//                clientSentence = inFromClient.readLine();
//                System.out.println("Received: " + clientSentence);
//                capitalizedSentence = clientSentence.toUpperCase() + '\n';
//                outToClient.writeBytes(capitalizedSentence);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
