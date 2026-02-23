package imageprocessing;

import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayS16;
import boofcv.struct.image.GrayU8;


public class Convolution {

  public static void meanFilter(GrayU8 input, GrayU8 output, int size) {
        int r = 0;
    	for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                for (int z = 0; z < size; ++z) {
                    for (int t = 0; t < size; ++t) {
                        r = r + input.get(x, y);
                    }
                }
                r = 0
            }
        }
  }

  public static void convolution(GrayU8 input, GrayS16 output, int[][] kernel) {
      // TODO
  }

  public static void gradientImage(GrayU8 input, GrayU8 output, int[][] kernelX, int[][] kernelY){
      // TODO
  }

  public static void gradientImageSobel(GrayU8 input, GrayU8 output){
    int[][] kernelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int[][] kernelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    gradientImage(input, output, kernelX, kernelY);
  }

  public static void gradientImagePrewitt(GrayU8 input, GrayU8 output){
      // TODO
  }

  public static void gradientOrientation(GrayU8 input, GrayU8 output, int[][] kernelX, int[][] kernelY){
        // TODO
  }

  public static int[] gradientHistogram(GrayU8 input, int nBins) {
        // TODO
  }

  public static double gradientHistogramDistance(GrayU8 input1, GrayU8 input2, int nBins){
        // TODO
  }
    
  public static void main(final String[] args) {
    // load image
    if (args.length < 2) {
      System.out.println("missing input or output image filename");
      System.exit(-1);
    }
    final String inputPath = args[0];
    GrayU8 input = UtilImageIO.loadImage(inputPath, GrayU8.class);
    GrayU8 output = input.createSameShape();

    // processing
    meanFilter(input, output, 11);
    
    // save output image
    final String outputPath = args[1];
    UtilImageIO.saveImage(output, outputPath);
    System.out.println("Image saved in: " + outputPath);
  }

}
