package de.hs_mannheim.informatik.bdea.gruppe2.LambdaTagCloud.service;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
import scala.Tuple2;

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

  public void createTagCloud(String filename, List<Tuple2<String,Double>> tfIdfList) {
    List<WordFrequency> convertedTfIdfList = new ArrayList<>();
    tfIdfList.forEach(t -> {
      convertedTfIdfList.add(new WordFrequency(t._1, (int) Math.round(t._2)));
    });
    final Dimension dimension = new Dimension(800, 800);
    final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
    Color[] colorPalette = {
        new Color(0xFFFFFF),
        new Color(0xE7DDFE),
        new Color(0xD4BAFA),
        new Color(0xC795F1),
        new Color(0xBF6CE3)
    };

    wordCloud.setPadding(2);
    wordCloud.setBackground(new CircleBackground(400));
    wordCloud.setBackgroundColor(new Color(0x303030));
    wordCloud.setColorPalette(new ColorPalette(colorPalette));
    wordCloud.setFontScalar(new SqrtFontScalar(12, 60));
    wordCloud.build(convertedTfIdfList);
    wordCloud.writeToFile(FileService.TAG_CLOUD_PATH + filename + ".png");
  }

  private String getBase64ImageFromFile(File file) throws IOException {
    byte[] imageData = Files.readAllBytes(file.toPath());
    return Base64.getEncoder().encodeToString(imageData);
  }
}