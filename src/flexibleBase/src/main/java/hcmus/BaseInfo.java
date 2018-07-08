package hcmus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaseInfo {
    private int id;
    private String name;
    private List<File> files;
    private List<String> fileNameList = new ArrayList<>();
    private String fileNames;
    private SOCKET_TYPE type;

    public BaseInfo() {
    }

    public BaseInfo(int id, SOCKET_TYPE type, String name, List<String> fileNameList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.fileNameList = fileNameList;
    }

    public BaseInfo(int id, List<File> files, SOCKET_TYPE type) {
        this.id = id;
        this.name = String.format("NODE_%d_%d", id, files.size());
        this.type = type;
        this.files = files;
        this.fileNames = "";

        List<String> arr = new ArrayList<>();
        for (File file : files) {
            arr.add(file.getName());
            fileNames+= file.getName()+ ";";
        }
        this.fileNameList.addAll(arr);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getFileNames() {
        return fileNameList;
    }

    public List<File> getFiles() {
        return files;
    }

    public String parseToString() {
        String json = String.format("id:%d - type:%s - name:%s - files:%s", this.id, type.name(), this.name, this.fileNames);
        return json;
    }

    public static BaseInfo parseToObject(String json) {
        String[] paragraphs = json.split(" - ");
        String sId = paragraphs[0].replace("id:", "").trim();
        int id = Integer.parseInt(sId);
        SOCKET_TYPE type = SOCKET_TYPE.NODE;
        if (paragraphs[1].contains(SOCKET_TYPE.CLIENT.name())) {
            type = SOCKET_TYPE.CLIENT;
        }

        String nodeName = paragraphs[2].replace("name:", "").trim();

        List<String> files = new ArrayList<>();
        String sFile = paragraphs[3].replace("files:", "").trim();
        if (sFile.contains(";")) {
            String[] names = sFile.trim().split(";");
            for (String name : names) {
                if (name != "") {
                    files.add(name);
                }
            }
        }

        BaseInfo info = new BaseInfo(id, type, nodeName, files);
        return info;
    }
}
