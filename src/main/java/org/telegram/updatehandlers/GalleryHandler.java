package org.telegram.updatehandlers;

import org.telegram.datatypes.MessageType;
import org.telegram.structure.NewMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryHandler {
    private static final String LOCAL_GALLERY_PATH = "E:\\Proton\\My files\\Gallery\\zzzzz\\Zara";
//    private static final String LOCAL_GALLERY_PATH = "E:\\Proton\\My files\\Gallery";

    public List<NewMessage> readAllGallery() {
        List<File> allFiles = new ArrayList<>();
        File galleryFolder = new File(LOCAL_GALLERY_PATH);
        if (galleryFolder.exists())
            getAllFiles(galleryFolder, allFiles);
        List<NewMessage> a = new ArrayList<>();
        for (File file : allFiles) {
            a.add(new NewMessage()
                    .withMessageType(MessageType.PHOTO)
                    .withPhotoUrl(file.getAbsolutePath()));
        }
        return a;
    }

    private void getAllFiles(File folder, List<File> allFiles) {
        final File[] folderFiles = folder.listFiles();
        if (Objects.nonNull(folderFiles)) {
            for (File folderItem : folderFiles) {
                if (folderItem.isDirectory()) getAllFiles(folderItem, allFiles);
                else if (folderItem.isFile()) allFiles.add(folderItem);
            }
        }
    }
}
