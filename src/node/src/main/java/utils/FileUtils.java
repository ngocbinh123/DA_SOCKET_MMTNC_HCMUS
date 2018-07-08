package utils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    /**
     * list files in the given directory and subdirs (with recursion)
     * @param paths
     * @return
     */
    public static List<File> getFiles(String paths) {
        List<File> filesList = new ArrayList<File>();
        for (final String path : paths.split(File.pathSeparator)) {
            final File file = new File(path);
            if( file.isDirectory()) {
                recurse(filesList, file);
            }
            else {
                filesList.add(file);
            }
        }
        return filesList;
    }

    private static void recurse(List<File> filesList, File f) {
        File list[] = f.listFiles();
        for (File file : list) {
            if (file.isDirectory()) {
                recurse(filesList, file);
            }
            else {
                filesList.add(file);
            }
        }
    }

    /**
     * List the content of the given jar
     * @param jarPath
     * @return
     * @throws IOException
     */
    public static List<String> getJarContent(String jarPath) throws IOException {
        List<String> content = new ArrayList<String>();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            String name = entry.getName();
            content.add(name);
        }
        return content;
    }


    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

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
    public static void main(String args[]) throws Exception {
    }
}
