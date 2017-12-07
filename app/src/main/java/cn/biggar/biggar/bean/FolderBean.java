package cn.biggar.biggar.bean;

/**
 * Created by SUE on 2016/6/23 0023.
 */
public class FolderBean {

    private String folderName;
    private String folderPath;
    private int fileCount;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
}
