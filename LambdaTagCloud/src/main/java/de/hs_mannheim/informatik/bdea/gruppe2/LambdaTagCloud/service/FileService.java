package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

  public static final String FILE_PATH = "files/";
  public static final String TAG_CLOUD_PATH = "tag_clouds/";

  FileService() {
    File textFiles = new File(FILE_PATH);
    File path = new File(TAG_CLOUD_PATH);
		
		if(!textFiles.exists())
			textFiles.mkdir();
    if (!path.exists())
      path.mkdir();
  }
  
  public void saveFile(MultipartFile file) throws IOException {
    String fileName = file.getOriginalFilename();
    File newFile = new File(FILE_PATH + fileName);
    Files.copy(file.getInputStream(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  
  }

  public File[] getTagCloudImages() {
    return new File(TAG_CLOUD_PATH).listFiles();
  }

  public File[] getAllDocuments() {
    return new File(FILE_PATH).listFiles();
  }

  public File getTagCloudImage(String name) {
    return new File(TAG_CLOUD_PATH + name + ".png");
  }

}
