package se233.project.controller;

import se233.project.controller.customexceptions.InvalidFileTypeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {
    public static void unzip(File zipfile, List<File> files) throws InvalidFileTypeException {
        String tempDest = ".\\temp\\";
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            List<String> invalidFiles = new ArrayList<>();
            while ((entry = zis.getNextEntry()) != null) {
                String entryExtension = entry.getName().substring(entry.getName().lastIndexOf(".")+1);
                if (entryExtension.equals("jpg") || entryExtension.equals("png")) {
                    File file = new File(tempDest + File.separator + entry.getName());
                    if (entry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        (new File(file.getParent())).mkdirs();
                        FileOutputStream fos = new FileOutputStream(file);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        files.add(file);
                    }
                } else {
                    invalidFiles.add(entry.getName());
                }
            }
            if (invalidFiles.size() > 0) {
                throw new InvalidFileTypeException(invalidFiles);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
