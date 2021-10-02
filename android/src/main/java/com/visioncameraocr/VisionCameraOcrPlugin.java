package com.visioncameraocr;

import androidx.camera.core.ImageProxy;
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;

public class VisionCameraOcrPlugin extends FrameProcessorPlugin {

  @Override
  public Object callback(ImageProxy image, Object[] params) {
    // code goes here
    return "test";
  }

  VisionCameraOcrPlugin() {
    super("scanOCR");
  }
}
