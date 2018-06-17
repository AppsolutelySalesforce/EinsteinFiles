package com.einstein.files.EiFiles;

import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface ZipService {

    File generateZip(Map<String, S3Object> s3Objects, String zipName) throws IOException;
}
