package hcmus.RUDP;

import hcmus.Constant;

import java.io.*;
import java.net.*;

public class UDPSender {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private int mReceiverPort;
    private String mReceiverIP;

    public UDPSender(int receiverPort, String receiverIP) {
        this.mReceiverPort = receiverPort;
        this.mReceiverIP = receiverIP;
    }

    public void connectServer() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(File fileSend) {
        if (clientSocket == null) {
            connectServer();
        }
        System.out.println("send file to client with port: " + String.valueOf(mReceiverPort) + " host: " + mReceiverIP);

        InetAddress inetAddress;
        DatagramPacket sendPacket;

        try {
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            inetAddress = InetAddress.getByName(mReceiverIP);
            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];

            // get file size
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);

            // check last bytes of file
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // split file into pieces and assign to fileBytes
            byte[][] fileBytes = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
            int count = 0;
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytes[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }

            // read file info
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(fileSend.getName());
            fileInfo.setFileSize(fileSend.length());
            fileInfo.setPiecesOfFile(piecesOfFile);
            fileInfo.setLastByteLength(lastByteLength);

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(),
                    baos.toByteArray().length, inetAddress, mReceiverPort);
            clientSocket.send(sendPacket);

            // send file content
            System.out.println("Sending file...");
            // send pieces of file
            for (int i = 0; i < (count - 1); i++) {
                sendPacket = new DatagramPacket(fileBytes[i], PIECES_OF_FILE_SIZE,
                        inetAddress, mReceiverPort);
                clientSocket.send(sendPacket);
                waitMillisecond(40);
            }
            // send last bytes of file
            sendPacket = new DatagramPacket(fileBytes[count - 1], PIECES_OF_FILE_SIZE,
                    inetAddress, mReceiverPort);
            clientSocket.send(sendPacket);
            waitMillisecond(40);

            // close stream
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent.");
    }

    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String sentence) {
        DatagramSocket clientSocket;
        try {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(mReceiverIP);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[Constant.MAX_BYTE];
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, mReceiverPort);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
