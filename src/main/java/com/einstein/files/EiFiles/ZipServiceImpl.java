package com.einstein.files.EiFiles;

import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipServiceImpl implements ZipService {

    @Override
    public File generateZip(Map<String, S3Object> s3Objects, String zipName) throws IOException {

        File f = new File(zipName);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));

        for (String s3o : s3Objects.keySet()) {

            ZipEntry e = new ZipEntry(s3o);
            out.putNextEntry(e);

            byte[] data = IOUtils.toByteArray(s3Objects.get(s3o).getObjectContent());

            out.write(data, 0, data.length);
            out.closeEntry();
        }

        out.close();
        return f;
    }

}
