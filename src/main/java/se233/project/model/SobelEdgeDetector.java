/**************************************************************************
 * @author Jason Altschuler
 *
 * @tags edge detection, image analysis, computer vision, AI, machine learning
 *
 * PURPOSE: Edge detector
 *
 * ALGORITHM: Sobel edge detector algorithm
 *
 * For full documentation, see the README
  ************************************************************************/

package se233.project.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import se233.project.controller.grayscale.Grayscale;
import se233.project.controller.ui.ImageViewer;
import se233.project.controller.util.Threshold;


public class SobelEdgeDetector extends GaussianEdgeDetector {
   
   /*********************************************************************
    * Convolution kernels
    *********************************************************************/
   private final static double[][] X_kernel3x3 = {{-1, 0, 1},
                                               {-2, 0, 2},
                                               {-1, 0, 1}};

   private final static double[][] Y_kernel3x3 = {{1, 2, 1},
                                               {0, 0, 0},
                                               {-1, -2, -1}};

   private final static double[][] X_kernel5x5 = {{2, 2, 4, 2, 2},
                                               {1, 1, 2, 1, 1},
                                               {0, 0, 0, 0, 0},
                                               {-1, -1, -2, -1, -1},
                                               {-2, -2, -4, -2, -2}};

   private final static double[][] Y_kernel5x5 = {{2, 1, 0, -1, -2},
                                               {2, 1, 0, -1, -2},
                                               {4, 2, 0, -2, -4},
                                               {2, 1, 0, -1, -2},
                                               {2, 1, 0, -1, -2}};


   private static double[][] X_kernel;
   private static double[][] Y_kernel;

   /*********************************************************************
    * Implemented abstract methods
    *********************************************************************/

   /**
    * @Override
    * {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}
    */
   public double[][] getXkernel() {
      return SobelEdgeDetector.X_kernel;
   }
   
   /**
    * @Override
    * {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}
    */
   public double[][] getYkernel() {
      return SobelEdgeDetector.Y_kernel;
   }

   
   /*********************************************************************
    * Constructor 
    **********************************************************************/
   
   /**
    * All work is done in constructor.
    * @param filePath path to image
    */
   public SobelEdgeDetector(String filePath, String kernelSize, boolean defaultThreshold, int threshold) {
      // read image and get pixels
      BufferedImage originalImage;
      if (kernelSize.equals("3x3")) {
         X_kernel = X_kernel3x3;
         Y_kernel = Y_kernel3x3;
      } else {
         X_kernel = X_kernel5x5;
         Y_kernel = Y_kernel5x5;
      }
      try {
         originalImage = ImageIO.read(new File(filePath));
         findEdges(Grayscale.imgToGrayPixels(originalImage), false, defaultThreshold, threshold);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   /**
    * All work is done in constructor.
    * <P> Uses L2 norm by default.
    * @param image
    */
   public SobelEdgeDetector(int[][] image, String kernelSize, boolean defaultThreshold, int threshold) {
      if (kernelSize.equals("3x3")) {
         X_kernel = X_kernel3x3;
         Y_kernel = Y_kernel3x3;
      } else {
         X_kernel = X_kernel5x5;
         Y_kernel = Y_kernel5x5;
      }
      findEdges(image, false, defaultThreshold, threshold);
   }
   
   /**
    * All work is done in constructor. 
    * <P> Gives option to use L1 or L2 norm.
    */
   public SobelEdgeDetector(int[][] image, String kernelSize, boolean L1norm, boolean defaultThreshold, int threshold) {
      if (kernelSize.equals("3x3")) {
         X_kernel = X_kernel3x3;
         Y_kernel = Y_kernel3x3;
      } else {
         X_kernel = X_kernel5x5;
         Y_kernel = Y_kernel5x5;
      }
      findEdges(image, L1norm, defaultThreshold, threshold);
   }


   /*********************************************************************
    * Unit testing
    * @throws IOException 
    *********************************************************************/

//   /**
//    * Example run.
//    * <P> Displays detected edges next to orignal image.
//    * @param args
//    * @throws IOException
//    */
//   public static void main(String[] args) throws IOException {
//      // read image and get pixels
//      String img = args[0];
//      BufferedImage originalImage = ImageIO.read(new File(img));
//      int[][] pixels = Grayscale.imgToGrayPixels(originalImage);
//
//      // run SobelEdgeDetector
//      final long startTime = System.currentTimeMillis();
//      SobelEdgeDetector sed = new SobelEdgeDetector(pixels);
//      final long endTime = System.currentTimeMillis();
//
//      // print timing information
//      final double elapsed = (double) (endTime - startTime) / 1000;
//      System.out.println("Sobel Edge Detector took " + elapsed + " seconds.");
//      System.out.println("Threshold = " + sed.threshold);
//
//      // display edges
//      boolean[][] edges = sed.getEdges();
//      BufferedImage edges_image = Threshold.applyThresholdReversed(edges);
//      BufferedImage[] toShow = {originalImage, edges_image};
//      String title = "Sobel Edge Detector by Jason Altschuler";
//      ImageViewer.showImages(toShow, title);
//   }

}
