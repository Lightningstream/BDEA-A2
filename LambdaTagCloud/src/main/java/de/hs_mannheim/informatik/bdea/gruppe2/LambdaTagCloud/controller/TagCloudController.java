package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.TagCloud;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service.FileService;
import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service.TagCloudService;

@RestController
public class TagCloudController {

  private final TagCloudService tagCloudService;
  private final FileService fileService;

  public TagCloudController(TagCloudService tagCloudService, FileService fileService) {
    this.tagCloudService = tagCloudService;
    this.fileService = fileService;
  }

  @GetMapping("/TagCloud")
  public TagCloud[] getAllTagClouds() throws IOException {
    return this.tagCloudService.getAllTagClouds();
  }

  @GetMapping("/TagCloud/{name}")
  public TagCloud GetTagCloudByName(@PathVariable("name") String name) throws IOException {
    return this.tagCloudService.getTagCloudByName(name);
  }

  @PostMapping("/TagCloud")
  public TagCloud uploadTagCloud(MultipartFile file) throws IOException {
    this.fileService.saveFile(file);
    final String fileName = file.getOriginalFilename().replace(".txt", "");
    this.tagCloudService.createTagCloud(fileName, new String(file.getBytes()));
    return this.tagCloudService.getTagCloudByName(fileName);
  }

  @GetMapping("/Batch")
  public String startBatch() {
    // Run Batch processing here
    return "Started batch";
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOError(IOException ex) {
    System.out.println(ex + ex.toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
  }

}
