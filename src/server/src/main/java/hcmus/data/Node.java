package hcmus.data;

import java.net.Socket;
import java.util.List;

public class Node {
    private int id;
    private String name;
    private Socket socket;
    private List<String> fileNames;

    public Node(int id, String name, Socket socket, List<String> fileNames) {
        this.id = id;
        this.name = name;
        this.socket = socket;
        this.fileNames = fileNames;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
