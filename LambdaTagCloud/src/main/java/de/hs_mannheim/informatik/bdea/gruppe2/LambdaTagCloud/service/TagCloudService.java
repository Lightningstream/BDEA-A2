package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.palette.ColorPalette;

import de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.model.TagCloud;

@Service
public class TagCloudService {

  private final FileService fileService;
  private final DocumentAnalyzerService documentAnalyzerService;

  public TagCloudService(FileService fileService, DocumentAnalyzerService documentAnalyzerService) {
    this.fileService = fileService;
    this.documentAnalyzerService = documentAnalyzerService;
  }

  public TagCloud[] getAllTagClouds() throws IOException {
    File[] files = this.fileService.getTagCloudImages();
    TagCloud[] clouds = new TagCloud[files.length];

    for (int i = 0; i < files.length; i++) {
      if (files[i].getName().endsWith(".png")) {
        String name = files[i].getName();
        String base64Image = this.getBase64ImageFromFile(files[i]);
        clouds[i] = new TagCloud(name.replace(".png", ""), base64Image);
      }
    }
    return clouds;
  }

  public TagCloud getTagCloudByName(String name) throws IOException {
    File file = this.fileService.getTagCloudImage(name);
    String base64Image = this.getBase64ImageFromFile(file);
    return new TagCloud(name, base64Image);
  }

  public void createTagCloud(String filename, String content) {

    final List<WordFrequency> tfIdfList = documentAnalyzerService.getTfIdfList(content);
		final Dimension dimension = new Dimension(600, 600);
		final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
		wordCloud.setPadding(2);
		wordCloud.setBackground(new CircleBackground(300));
		wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
		wordCloud.setFontScalar(new SqrtFontScalar(8, 50));
		wordCloud.build(tfIdfList);
		wordCloud.writeToFile(FileService.TAG_CLOUD_PATH + filename + ".png");
  }

  private String getBase64ImageFromFile(File file) throws IOException {
    byte[] imageData = Files.readAllBytes(file.toPath());
    return Base64.getEncoder().encodeToString(imageData);
  }
}
