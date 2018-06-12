package com.einstein.files.EiFiles;

public interface S3Services {
    void downloadFile(String keyName);

    void uploadFile(String keyName, String uploadFileBAse64);

    String getURL(String keyName);
}
