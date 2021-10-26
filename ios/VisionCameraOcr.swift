import Vision
import MLKitTextRecognition
import MLKitVision
import CoreML

@objc(QRCodeFrameProcessorPlugin)
public class QRCodeFrameProcessorPlugin: NSObject, FrameProcessorPluginBase {

  static let textRecognizer = TextRecognizer.textRecognizer()

  private static func getImageFromSampleBuffer (buffer:CMSampleBuffer) -> UIImage? {
          if let pixelBuffer = CMSampleBufferGetImageBuffer(buffer) {
              let ciImage = CIImage(cvPixelBuffer: pixelBuffer)
              let uiImage = UIImage(ciImage: ciImage)
              let srcWidth = CGFloat(ciImage.extent.width)
              if (srcWidth <= 480) {
                return uiImage;
              }

              let srcHeight = CGFloat(ciImage.extent.height)
              let dstWidth: CGFloat = 480
              let ratio = dstWidth / srcWidth
              let dstHeight: CGFloat = srcHeight * ratio
              let imageSize = CGSize(width: srcWidth * ratio, height: srcHeight * ratio)
              UIGraphicsBeginImageContextWithOptions(imageSize, false, 1.0)
              uiImage.draw(in: CGRect(x: 0, y: 0, width: dstWidth, height: dstHeight))
              let newImage = UIGraphicsGetImageFromCurrentImageContext()
              UIGraphicsEndImageContext()
              return newImage
          }

          return nil
      }

  @objc
  public static func callback(_ frame: Frame!, withArgs _: [Any]!) -> Any! {
    let imageScaled = getImageFromSampleBuffer(buffer: frame.buffer)
    guard imageScaled != nil else {
        NSLog("NO IMAGE SCALED")
        return nil
    }
    let image = VisionImage.init(image: imageScaled!)
    image.orientation = frame.orientation
    var recognizedText: String
    do {
        recognizedText = try TextRecognizer.textRecognizer().results(in: image).text
        return recognizedText
       } catch let error {
        print("Failed to recognize text with error: ", error.localizedDescription)
        return nil
       }

     return nil
  }
}
