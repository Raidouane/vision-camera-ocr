package com.visioncameraocr;

import android.media.Image;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Tasks;
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import javax.annotation.Nonnull;

import java.util.concurrent.ExecutionException;

public class VisionCameraOcrPlugin extends FrameProcessorPlugin {

  @Override
  public Object callback(ImageProxy image, Object[] params) {
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    Image mediaImage = image.getImage();
    if (mediaImage != null) {
      InputImage imageInput = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

      try {
        System.out.println("Start PROCESS");
        Task<Text> result =
          recognizer.process(imageInput)
          .addOnFailureListener(
              new OnFailureListener() {
                @Override
                public void onFailure(@Nonnull Exception e) {
                  // Task failed with an exception
                  e.printStackTrace();
                }
              });


        Text visionText = Tasks.await(result);
        return visionText.getText();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      return "";
    }

    return null;
  }

  VisionCameraOcrPlugin() {
    super("scanOCR");
  }
}
