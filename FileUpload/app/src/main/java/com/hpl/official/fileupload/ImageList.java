package com.hpl.official.fileupload;

/**
 * Created by Pushpender Bhandari on 1/21/2016.
 */
public class ImageList {
    String FilePath;

    public ImageList(String filePath) {
        this.FilePath = filePath;
    }

    public ImageList() {
        this.FilePath = null;

    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
