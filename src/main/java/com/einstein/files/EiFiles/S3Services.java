package com.einstein.files.EiFiles;

import com.amazonaws.services.s3.model.S3Object;

public interface S3Services {
    S3Object downloadFile(String keyName);

    void uploadFile(String keyName, byte[] bites);

    void uploadFileBase64(String fileName, String uploadFileBase64);

    String getURL(String keyName);
}
