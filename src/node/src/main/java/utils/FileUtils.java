package utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    public List<File> getFiles(int nodeId) {
        File file;
        switch (nodeId) {
            case 1:
                file = new File(this.getClass().getResource("/node1").getFile());
                break;
            case 2:
                file = new File(this.getClass().getResource("/node2").getFile());
                break;
            case 3:
                file = new File(this.getClass().getResource("/node3").getFile());
                break;
                default:
                    file = new File(this.getClass().getResource("/node").getFile());
                    break;
        }
        return Arrays.asList(file.listFiles());
    }

    public static List<File> getFilesByNodeId(int nodeId) {
        return (new FileUtils()).getFiles(nodeId);
    }
}
