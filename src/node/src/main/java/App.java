import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class App {
    public static void main( String[] args ) {
        System.out.println( "Start data.Node" );
        NodeFrm.start();
    }
//    public static void main(String[] args) throws IOException {
//        // TODO code application logic here
//        String serverHostname = new String ("127.0.0.1");
//
//        if (args.length > 0)
//            serverHostname = args[0];
//        System.out.println ("Attemping to connect to host " +
//                serverHostname + " on port 10007.");
//
//        Socket echoSocket = null;
//        PrintWriter out = null;
//        BufferedReader in = null;
//
//        try {
//            // echoSocket = new Socket("taranis", 7);
//            echoSocket = new Socket(serverHostname, 10007);
//            out = new PrintWriter(echoSocket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(
//                    echoSocket.getInputStream()));
//        } catch (UnknownHostException e) {
//            System.err.println("Don't know about host: " + serverHostname);
//            System.exit(1);
//        } catch (IOException e) {
//            System.err.println("Couldn't get I/O for "
//                    + "the connection to: " + serverHostname);
//            System.exit(1);
//        }
//
//        BufferedReader stdIn = new BufferedReader(
//                new InputStreamReader(System.in));
//        String userInput= "";
//        System.out.println("Waiting for other currentSocket");
//        do
//        {
//            String signal = in.readLine();
//            if(signal.equals("0"))//Send message
//            {
//                System.out.print("Send: ");
//                userInput = stdIn.readLine();
//                out.println(userInput);
//            }
//            else if(signal.equals("1"))//Receive message
//            {
//                System.out.println("Receive: " + in.readLine());
//            }
//            if(userInput.equals("Bye."))
//                break;
//
//        }while(true);
//        out.close();
//        in.close();
//        stdIn.close();
//        echoSocket.close();
//    }
}
