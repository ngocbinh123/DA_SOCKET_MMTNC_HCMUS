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

    public BaseInfo(int id, SOCKET_TYPE type) {
        this(id, null, type);
    }

    public BaseInfo(int id, List<File> files, SOCKET_TYPE type) {
        this.id = id;
        if (type == SOCKET_TYPE.NODE) {
            this.name = String.format("NODE_%d_%d", id, files.size());
        }else {
            this.name = String.format("CLIENT_%d", id);
        }
        this.type = type;
        this.files = files;
        this.fileNames = "";

        if (files != null) {
            List<String> arr = new ArrayList<>();
            for (File file : files) {
                arr.add(file.getName());
                fileNames+= file.getName()+ ";";
            }
            this.fileNameList.addAll(arr);
        }
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

    public void setFileNames(List<String> newFileNames) {
        fileNameList = newFileNames;
    }

    public void addFilesNames(List<String> newFileNames) {
        fileNameList.addAll(newFileNames);
    }

    public List<File> getFiles() {
        return files;
    }

    public SOCKET_TYPE getType() {
        return type;
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

        String sFile = paragraphs[3].replace("files:", "").trim();
        List<String> files = getFilesFromJson(sFile);

        BaseInfo info = new BaseInfo(id, type, nodeName, files);
        return info;
    }

    public static List<String> getFilesFromJson(String sFile) {
        List<String> files = new ArrayList<>();
        if (sFile.contains(";")) {
            String[] names = sFile.trim().split(";");
            for (String name : names) {
                if (name != "") {
                    files.add(name);
                }
            }
        }

        return files;
    }
}
