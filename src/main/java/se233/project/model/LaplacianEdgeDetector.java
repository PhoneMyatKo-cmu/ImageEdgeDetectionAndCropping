/**************************************************************************
 * @author Jason Altschuler
 *
 * @tags edge detection, image analysis, computer vision, AI, machine learning
 *
 * PURPOSE: Edge detector
 *
 * ALGORITHM: Laplacian edge detector algorithm
 *
 * Finds edges by finding pixel intensities where the Laplacian operator
 * (divergence of gradient; 2nd order differential operator) is 0.
 * (Discrete image derivative found by image convolutions).  However, 
 * this method historically has been replaced by Sobel, Canny, etc. because 
 * it finds many false edges. The reason is that 2nd derivative could mean
 * a local min or max of first derivative. We only want the max's;
 * the mins are false edges.
 *
 * For full documentation, see the README
 ************************************************************************/

package se233.project.model;

import se233.project.controller.grayscale.Grayscale;
import se233.project.controller.imagederivatives.ConvolutionKernel;
import se233.project.controller.imagederivatives.ImageConvolution;
import se233.project.controller.util.Threshold;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class LaplacianEdgeDetector extends EdgeDetector {

    /************************************************************************
     * Data structures
     ***********************************************************************/

    // dimensions are slightly smaller than original image because of discrete convolution.
    private boolean[][] edges;

    // threshold used to find edges; one requirement for [i,j] to be edge is |G[i,j]| = |f'[i,j]| > threshold.
    private int threshold;

    // convolution kernel; discretized appromixation of 2nd derivative
    private double[][] kernel3x3 = {{-1, -1, -1},
            {-1, 8, -1},
            {-1, -1, -1}};

    private double[][] kernel5x5 = {{0, 0, 1, 0, 0},
            {0, 1, 2, 1, 0},
            {1, 2, -17, 2, 1},
            {0, 1, 2, 1, 0},
            {0, 0, 1, 0, 0}};

    private double[][] kernel;
    /***********************************************************************
     * Detect edges
     ***********************************************************************/

    /**
     * All work is done in constructor.
     *
     * @param filePath path to image
     */
    public LaplacianEdgeDetector(String filePath, String kernelSize, boolean defaultThreshold, int threshold) {
        if (kernelSize.equals("3x3")) {
            kernel = kernel3x3;
        } else {
            kernel = kernel5x5;
        }
        // read image and get pixels
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File(filePath));
            findEdges(Grayscale.imgToGrayPixels(originalImage), defaultThreshold, threshold);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Find beautiful edges.
     * <P> All work is done in constructor.
     *
     * @param image
     */
    public LaplacianEdgeDetector(int[][] image, String kernelSize, boolean defaultThreshold, int threshold) {
        if (kernelSize.equals("3x3")) {
            kernel = kernel3x3;
        } else {
            kernel = kernel5x5;
        }
        findEdges(image, defaultThreshold, threshold);
    }


    /**
     * Finds only the most beautiful edges.
     *
     * @param image
     */
    private void findEdges(int[][] image, boolean defaultThreshold, int threshold) {
        // convolve image with Gaussian kernel
        ImageConvolution gaussianConvolution = new ImageConvolution(image, ConvolutionKernel.GAUSSIAN_KERNEL);
        int[][] smoothedImage = gaussianConvolution.getConvolvedImage();

        // apply convolutions to smoothed image
        ImageConvolution ic = new ImageConvolution(smoothedImage, kernel);

        // calculate magnitude of gradients
        int[][] convolvedImage = ic.getConvolvedImage();
        int rows = convolvedImage.length;
        int columns = convolvedImage[0].length;

        // calculate threshold intensity to be edge
        if (defaultThreshold) {
            this.threshold = Threshold.calcThresholdEdges(convolvedImage);
        } else {
            this.threshold = threshold;
        }

        // threshold image to find edges
        edges = new boolean[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                edges[i][j] = Math.abs(convolvedImage[i][j]) > this.threshold;
    }


    /*********************************************************************
     * Accessors
     *********************************************************************/

    public boolean[][] getEdges() {
        return edges;
    }

    public int getThreshold() {
        return threshold;
    }


    /*********************************************************************
     * Unit testing and display
     *********************************************************************/

//   /**
//    * Example run.
//    * <P> Displays detected edges next to orignal image.
//    * @param args
//    * @throws IOException
//    */
//   public static void main(String[] args) throws IOException {
//      // read image and get pixels
//      String imageFile = "D:\\User\\OneDrive\\Pictures\\Download Pictures\\Lion.jpg";
//      BufferedImage originalImage = ImageIO.read(new File(imageFile));
//      int[][] pixels = Grayscale.imgToGrayPixels(originalImage);
//
//      // run Laplacian edge detector
//      LaplacianEdgeDetector led = new LaplacianEdgeDetector(pixels);
//
//      // get edges
//      boolean[][] edges = led.getEdges();
//
//      // make images out of edges
//      BufferedImage laplaceImage = Threshold.applyThresholdReversed(edges);
//
//      // display edges
//      BufferedImage[] toShow = {originalImage, laplaceImage};
//      String title = "Laplace Edge Detection by Jason Altschuler";
//      ImageViewer.showImages(toShow, title);
//   }
}
