package hcmus.data;

import java.io.File;

public class NodeFile {
    private String name;
    private String nodeId;
    private String nodeIP;
    private int nodePort;
    private int nodeLocalPort;
    private String json;
    private File localFile;

    public NodeFile(String json) {
        this.json = json;
    }

    public NodeFile(String name, String nodeId, String nodeIP, String nodePort, String nodeLocalPort) {
        this.name = name;
        this.nodeId = nodeId;
        this.nodeIP = nodeIP;
        this.nodePort = Integer.parseInt(nodePort);
        this.nodeLocalPort = Integer.parseInt(nodeLocalPort);
    }

    public File getLocalFile() {
        return localFile;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    public String getName() {
        return name;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeIP() {
        return nodeIP;
    }

    public int getNodePort() {
        return nodePort;
    }

    public int getNodeLocalPort() {
        return nodeLocalPort;
    }
}
