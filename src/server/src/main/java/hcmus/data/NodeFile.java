package hcmus.data;

public class NodeFile {
    private Node node;
    private String name;

    public NodeFile(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNodeName() {
        return node.getName();
    }
}
