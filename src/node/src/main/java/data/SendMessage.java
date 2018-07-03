package data;

import hcmus.Constant;
import hcmus.SOCKET_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SendMessage extends Thread {
    private Socket currentSocket;
//    private String buffer;
    private BufferedReader in;
    protected PrintWriter out;
    public SendMessage(Socket socket) {
        this.currentSocket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.currentSocket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();
    }

    public void run(String condition) {
        if (out == null)
            return;

        try {
            String buffer;
            do {
                buffer = in.readLine();
                if (buffer.equalsIgnoreCase(Constant.MSG_WHO_ARE_YOU)) {
                    printOnConsole(buffer, SOCKET_TYPE.SERVER);
                    printOnConsole("I'm Node.", SOCKET_TYPE.NODE);
                }else if (buffer.equalsIgnoreCase(Constant.MSG_PLS_SEND_YOUR_FILES)) {
                    String msg = Constant.MSG_PLS_SEND_YOUR_FILES + ": ";
                    List<String> files = getFiles();
                    for (String file : files) {
                        msg+= file + ";";
                    }
                    out.println(msg);
                    printOnConsole(msg.replace( Constant.MSG_PLS_SEND_YOUR_FILES + ": ", ""), SOCKET_TYPE.NODE);
                }
            }while (!condition.equals(buffer));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printOnConsole(String msg, SOCKET_TYPE type) {
        System.out.println(String.format("%s: %s", type.name(), msg));
    }

    private List<String> getFiles() {
        List<String> arr = new ArrayList<String>();
        int size = new Random().nextInt(10);
        for (int i = 1; i < size; i++) {
            arr.add(String.format("FILE_%d_%d.pdf", currentSocket.getPort(), i));
        }

        return arr;
    }
}
