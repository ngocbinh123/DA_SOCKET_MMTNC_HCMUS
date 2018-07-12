package hcmus.RUDP;

import hcmus.Constant;

import java.io.*;
import java.net.*;

public class UDPSender {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private int serverPort = 6677;
    private String serverHost = "localhost";

    public static void main(String[] args) {
        String sourcePath = "/Users/Binh.Nguyen/Documents/nnbinh/hcmus/DA_SOCKET_MMTNC_HCMUS/src/node/target/classes/node1/node_1_document_2.docx";
        String destinationDir = "/Users/Binh.Nguyen/Documents/nnbinh/hcmus/DA_SOCKET_MMTNC_HCMUS/src/";
        UDPSender udpClient = new UDPSender(54176, Constant.LOCAL_HOST_NAME);
        udpClient.connectServer();
        udpClient.sendMessage(String.format("%s:%s",Constant.MSG_PLS_SEND_YOUR_FILES, "node_1_document_2.docx"));
//        udpClient.sendMessage(Constant.MSG_PLS_SEND_YOUR_FILES + ":");
//        udpClient.sendFile(sourcePath, destinationDir);
    }

    public UDPSender(int serverPort, String serverHost) {
        this.serverPort = serverPort;
        this.serverHost = serverHost;
    }

    public void connectServer() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(File fileSend, String destinationDir) {
        if (clientSocket == null) {
            connectServer();
        }
        System.out.println("send file to client with port: " + String.valueOf(serverPort) + " host: " + serverHost);

        InetAddress inetAddress;
        DatagramPacket sendPacket;

        try {
//            File fileSend = new File(sourcePath);
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            inetAddress = InetAddress.getByName(serverHost);
            byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];

            // get file size
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);

            // check last bytes of file
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // split file into pieces and assign to fileBytess
            byte[][] fileBytess = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
            int count = 0;
            while (bis.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                fileBytess[count++] = bytePart;
                bytePart = new byte[PIECES_OF_FILE_SIZE];
            }

            // read file info
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilename(fileSend.getName());
            fileInfo.setFileSize(fileSend.length());
            fileInfo.setPiecesOfFile(piecesOfFile);
            fileInfo.setLastByteLength(lastByteLength);
            fileInfo.setDestinationDirectory(destinationDir);

            // send file info
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(fileInfo);
            sendPacket = new DatagramPacket(baos.toByteArray(),
                    baos.toByteArray().length, inetAddress, serverPort);
            clientSocket.send(sendPacket);

            // send file content
            System.out.println("Sending file...");
            // send pieces of file
            for (int i = 0; i < (count - 1); i++) {
                sendPacket = new DatagramPacket(fileBytess[i], PIECES_OF_FILE_SIZE,
                        inetAddress, serverPort);
                clientSocket.send(sendPacket);
                waitMillisecond(40);
            }
            // send last bytes of file
            sendPacket = new DatagramPacket(fileBytess[count - 1], PIECES_OF_FILE_SIZE,
                    inetAddress, serverPort);
            clientSocket.send(sendPacket);
            waitMillisecond(40);

            // close stream
            bis.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
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
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(Constant.LOCAL_HOST_NAME);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[Constant.MAX_BYTE];
//            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
            clientSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
