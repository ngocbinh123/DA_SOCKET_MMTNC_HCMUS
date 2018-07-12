package hcmus.RUDP;

import hcmus.Constant;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPReceiver {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket serverSocket;
    private int port = 6677;

    public static void main(String[] args) {
        UDPReceiver udpServer = new UDPReceiver(Constant.UDP_PORT);
        udpServer.listeningMessage(new ReceiveListenMessage() {
            @Override
            public void onReceived(String message) {

            }

            @Override
            public void onError(String err) {

            }
        });
//        udpServer.startListen();
    }

    public UDPReceiver(int port) {
        this.port = port;
    }

    public void startListen(ReceiveListener listener) {
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("Server is opened on port " + port);
            listening(listener);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void listening(ReceiveListener listener) {
        while (true) {
            receiveFile(listener);
        }
    }

    public interface ReceiveListenMessage {
        void onReceived(String message);
        void onError(String err);
    }

    public void listeningMessage(ReceiveListenMessage listen) {
        try {
            serverSocket = new DatagramSocket(port);
            System.out.println("Server is opened on port " + port);
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                listen.onReceived(sentence.trim());
            }
        } catch (SocketException e) {
            e.printStackTrace();
            listen.onError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            listen.onError(e.getMessage());
        }
    }

    public interface ReceiveListener {
        void onReceived(File file);
        void failure(String err);
    }
    public void receiveFile(final ReceiveListener listener) {
        byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
        DatagramPacket receivePacket;

        try {
            // get file info
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            InetAddress inetAddress = receivePacket.getAddress();
            ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            FileInfo fileInfo = (FileInfo) ois.readObject();
            // show file info
            if (fileInfo != null) {
                System.out.println("File name: " + fileInfo.getFilename());
                System.out.println("File size: " + fileInfo.getFileSize());
                System.out.println("Pieces of file: " + fileInfo.getPiecesOfFile());
                System.out.println("Last bytes length: " + fileInfo.getLastByteLength());
            }
            // get file content
            System.out.println("Receiving file...");
            File fileReceive = new File(fileInfo.getDestinationDirectory()
                    + fileInfo.getFilename());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileReceive));
            // write pieces of file
            for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length, inetAddress, port);
                serverSocket.receive(receivePacket);
                bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
            }
            // write last bytes of file
            receivePacket = new DatagramPacket(receiveData, receiveData.length,
                    inetAddress, port);
            serverSocket.receive(receivePacket);
            bos.write(receiveData, 0, fileInfo.getLastByteLength());
            bos.flush();
            System.out.println("Done!");
            // close stream
            bos.close();
            listener.onReceived(fileReceive);
        } catch (IOException e) {
            e.printStackTrace();
            listener.failure(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            listener.failure(e.getMessage());
        }
    }
}
