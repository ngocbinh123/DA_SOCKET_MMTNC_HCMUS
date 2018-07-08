package hcmus;

public enum SOCKET_STATUS {
    CONNECT(1, "Connect"), DISCONNECT(2, "Disconnect");

    private final int key;
    private final String value;

    SOCKET_STATUS(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
