
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ImageDisplayzz {
    JPanel overlayPanel;

    JFrame frame;
    JLabel lbIm1;
    BufferedImage imgOne;
    BufferedImage imageTwo;
//    double S1 =0.125;
    int width2 = 7680; // default image width and height
    int height2 = 4320;
//    int len_S= (int) Math.ceil(1.0 / S1);
//    //	int width = 1920; // default image width and height
////	int height = 1080;
//    int width = 7680/len_S; // default image width and height
//    int height = 4320/len_S;

    /** Read Image RGB
     *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
     */
    public void readImageRGB(int width, int height, String imgPath, BufferedImage img, String S,String A, String w)
    {
        try
        {
//            int width2 = 7680; // default image width and height
//            int height2 = 4320;
            double S1 = Double.parseDouble(S);

            int len_S= (int) Math.ceil(1.0 / S1);
            //	int width = 1920; // default image width and height
//	int height = 1080;

            int frameLength = width2*height2*3;


            File file = new File(imgPath);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            long len = frameLength;
            byte[] bytes = new byte[(int) len];

            raf.read(bytes);
//			double S=0.25;
            double ind_h= Math.ceil(height2* S1);
            double ind_w= Math.ceil(width2* S1);
//			int len_S= (int) Math.ceil(1.0 / S);
//            System.out.println("scale is:");
//            System.out.println(len_S);
            int hInd = (int) ind_h;
            int wInd = (int) ind_w;
            int inds=0;
//            System.out.println(wInd);
//            System.out.println(hInd);

            byte[][] reshapedArray_r = new byte[height2][width2]; // Create a 2D array with dimensions 2x10

            // Reshape the flat array into a 2D array
            for (int i = 0; i < height2; i++) {
                for (int j = 0; j < width2; j++) {
                    reshapedArray_r[i][j] = bytes[i * width2 + j];
                }
            }
            byte[][] reshapedArray_g = new byte[height2][width2]; // Create a 2D array with dimensions 2x10

            // Reshape the flat array into a 2D array
            for (int i = 0; i < height2; i++) {
                for (int j = 0; j < width2; j++) {
                    reshapedArray_g[i][j] = bytes[i * width2 + j+(width2*height2)];
                }
            }
            byte[][] reshapedArray_b = new byte[height2][width2]; // Create a 2D array with dimensions 2x10

            // Reshape the flat array into a 2D array
            for (int i = 0; i < height2; i++) {
                for (int j = 0; j < width2; j++) {
                    reshapedArray_b[i][j] = bytes[i * width2 + j+(2*width2*height2)];
                }
            }
            // Create an array to store the sums of each 4x4 patch
            int[][] patchSums_r = new int[height2/len_S][width2/len_S];

            for (int i = 0; i < height2; i += len_S) {
                for (int j = 0; j < width2; j += len_S) {
                    // Calculate the sum of the current 4x4 patch
                    int sum = 0;
                    for (int x = i; x < i + 1; x++) {
                        for (int y = j; y < j + 1; y++) {
                            sum += reshapedArray_r[x][y];
                        }
                    }

                    // Determine the patch's position in patchSums
                    int patchRow = i / len_S;
                    int patchCol = j / len_S;
                    patchSums_r[patchRow][patchCol] = sum;
                }
            }

            /////
            // Create an array to store the sums of each 4x4 patch
            int[][] patchSums_g = new int[height2/len_S][width2/len_S];

            for (int i = 0; i < height2; i += len_S) {
                for (int j = 0; j < width2; j += len_S) {
                    // Calculate the sum of the current 4x4 patch
                    int sum = 0;
                    for (int x = i; x < i + 1; x++) {
                        for (int y = j; y < j + 1; y++) {
                            sum += reshapedArray_g[x][y];
                        }
                    }

                    // Determine the patch's position in patchSums
                    int patchRow = i / len_S;
                    int patchCol = j / len_S;
                    patchSums_g[patchRow][patchCol] = sum;
                }
            }

            int[][] patchSums_b = new int[height2/len_S][width2/len_S];
            int q=0;
            for (int i = 0; i < height2; i += len_S) {
                for (int j = 0; j < width2; j += len_S) {
                    // Calculate the sum of the current 4x4 patch
                    int sum = 0;
                    for (int x = i; x < i + 1; x++) {
                        for (int y = j; y < j + 1; y++) {
                            sum += reshapedArray_b[x][y];
                        }
                    }

                    // Determine the patch's position in patchSums
                    int patchRow = i /len_S ;
                    int patchCol = j / len_S;
                    patchSums_b[patchRow][patchCol] = sum;
                    q++;
                }
            }

            // Define the size of the padded array
            int paddedRowCount = height+2; // 100 + 10 rows of padding
            int paddedColumnCount = width+2; // 200 + 20 columns of padding

            // Create a new padded array
            int[][] paddedArray_r = new int[paddedRowCount][paddedColumnCount];

            // Copy the original array into the padded array
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    paddedArray_r[i + 1][j + 1] = patchSums_r[i][j];
                }
            }
            int[][] paddedArray_g = new int[paddedRowCount][paddedColumnCount];

            // Copy the original array into the padded array
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    paddedArray_g[i + 1][j + 1] = patchSums_g[i][j];
                }
            }
            int[][] paddedArray_b = new int[paddedRowCount][paddedColumnCount];

            // Copy the original array into the padded array
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    paddedArray_b[i + 1][j + 1] = patchSums_b[i][j];
                }
            }



            int[][] inter_r = new int[height][width];
            int[][] inter_g = new int[height][width];
            int[][] inter_b = new int[height][width];

            int maxi=0;
            int mini=0;
            int b_a = 0xff;
            int g_a = 0xff;
            int r_a = 0xff;
            for (int i = 1; i < height+1; i ++){
                for (int j = 1; j < width+1; j ++){
                    b_a=(paddedArray_b[i][j]&0xff)+(paddedArray_b[i-1][j] &0xff)+(paddedArray_b[i+1][j]&0xff)+(paddedArray_b[i][j-1]&0xff)+(paddedArray_b[i][j+1]& 0xff) +(paddedArray_b[i+1][j+1]& 0xff)+(paddedArray_b[i-1][j+1] & 0xff)+(paddedArray_b[i+1][j-1]& 0xff)+(paddedArray_b[i-1][j-1]& 0xff);
//					r_a= ((paddedArray_r[i][j]>>16)&0xff) + ((paddedArray_r[i - 1][j]>>16)&0xff) + ((paddedArray_r[i + 1][j] >>16)&0xff)+ ((paddedArray_r[i][j - 1] >>16)&0xff) + ((paddedArray_r[i][j + 1]>>16)&0xff) + ((paddedArray_r[i + 1][j + 1] >>16)&0xff)+ ((paddedArray_r[i - 1][j + 1]>>16)&0xff) + ((paddedArray_r[i + 1][j - 1] >>16)&0xff)+ ((paddedArray_r[i - 1][j - 1]>>16)&0xff);
//					g_a=((paddedArray_g[i][j]>>8)&0xff) + ((paddedArray_g[i - 1][j] >>8)&0xff)+ ((paddedArray_g[i + 1][j]>>8)&0xff) + ((paddedArray_g[i][j - 1] >>8)&0xff)+ ((paddedArray_g[i][j + 1]>>8)&0xff) + ((paddedArray_g[i + 1][j + 1] >>8)&0xff)+ ((paddedArray_g[i - 1][j + 1] >>8)&0xff)+ ((paddedArray_g[i + 1][j - 1]>>8)&0xff) + ((paddedArray_g[i - 1][j - 1]>>8)&0xff);
                    r_a= (paddedArray_r[i][j]&0xff) + (paddedArray_r[i - 1][j]&0xff) + (paddedArray_r[i + 1][j] &0xff)+ (paddedArray_r[i][j - 1] &0xff) + (paddedArray_r[i][j + 1]&0xff) + (paddedArray_r[i + 1][j + 1] &0xff)+ (paddedArray_r[i - 1][j + 1] &0xff) + (paddedArray_r[i + 1][j - 1] &0xff)+ (paddedArray_r[i - 1][j - 1]&0xff);
                    g_a= (paddedArray_g[i][j]&0xff) + (paddedArray_g[i - 1][j]&0xff)+ (paddedArray_g[i + 1][j]&0xff) + (paddedArray_g[i][j - 1] &0xff)+ (paddedArray_g[i][j + 1]&0xff) + (paddedArray_g[i + 1][j + 1] &0xff)+ (paddedArray_g[i - 1][j + 1] &0xff)+ (paddedArray_g[i + 1][j - 1]&0xff) + (paddedArray_g[i - 1][j - 1]&0xff);

                    inter_r[i-1][j-1]=r_a/9;
                    inter_b[i-1][j-1]=b_a/9;
                    inter_g[i-1][j-1]=g_a/9;


                }
            }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
            int[][] rescaledImage_r = new int[height][width];

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            // Find the minimum and maximum pixel values in the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelValue = inter_r[y][x];
                    if (pixelValue < min) {
                        min = pixelValue;
                    }
                    if (pixelValue > max) {
                        max = pixelValue;
                    }
                }
            }

            // Rescale the pixel values to the range 0-255
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelValue = inter_r[y][x];
                    int rescaledValue = (int) (255.0 * (pixelValue - min) / (max - min));
                    rescaledImage_r[y][x] = rescaledValue;
                }
            }

            int[][] rescaledImage_g = new int[height][width];

            int min1 = Integer.MAX_VALUE;
            int max1 = Integer.MIN_VALUE;

            // Find the minimum and maximum pixel values in the image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixelValue = inter_g[y][x];
                    if (pixelValue < min1) {
                        min1 = pixelValue;
                    }
                    if (pixelValue > max1) {
                        max1 = pixelValue;
                    }
                }
            }





            int ind = 0;
            int param2 = Integer.parseInt(A);

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    if (param2==1){
                        byte a = 0;
                        int r = inter_r[y][x];
                        int g = inter_g[y][x];
                        int b = inter_b[y][x];




                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g& 0xff) << 8) | (b & 0xff);
                        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                        img.setRGB(x,y,pix);
                        ind++;

                    }
                    if (param2==0){
                        byte a = 0;
                        int r = patchSums_r[y][x];
                        int g = patchSums_g[y][x];
                        int b = patchSums_b[y][x];




                        int pix = 0xff000000 | ((r & 0xff) << 16) | ((g& 0xff) << 8) | (b & 0xff);
                        //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                        img.setRGB(x,y,pix);
                        ind++;


                    }




                }
            }
//            File outputFile = new File("/Users/mahtabmovahhedrad/Downloads/CSCI576/Assignment1_starter_code//16xHD_data_samples/output.jpg");
//
//            // Save the BufferedImage to the specified file
//            try {
//                ImageIO.write(img, "jpg", outputFile);
//                System.out.println("Image saved successfully.");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void showIms(String[] args) {

        // Read a parameter from command line
//        String param1 = args[1];
        String A=args[2];
        String S=args[1];
        String w=args[3];
        int win = Integer.parseInt(w);
        double S2 = Double.parseDouble(S);
        int len_S= (int) Math.ceil(1.0 / S2);
        //	int width = 1920; // default image width and height
//	int height = 1080;
        int width = 7680/len_S; // default image width and height
        int height = 4320/len_S;



// ç       System.out.println("The second parameter was: " + param1);

        // Read in the specified image

        imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, args[0], imgOne,S,A,w);
        imageTwo = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width2, height2, args[0], imageTwo,"1","0", w);


        // Use label to display the image
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);

        lbIm1 = new JLabel(new ImageIcon(imgOne));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        JFrame mouseImageFrame = new JFrame();
        frame.getContentPane().add(lbIm1, c);
        //================================================
        // Add a MouseListener to lbIm1
        lbIm1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();





                // Crop the 20x20 portion of the image centered at the mouse click


                // Create a new window for the 20x20 image
//                JFrame mouseImageFrame = new JFrame();
                mouseImageFrame.setSize(win, win);
                int xf=mouseX-win/2;
                int yf=mouseY;
                mouseImageFrame.setLocation(xf,yf); // Center the window

                // Create a JPanel to display the 20x20 image at the mouse's location
                JPanel mouseImagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        BufferedImage croppedImage = imageTwo.getSubimage((mouseX*len_S)-(win/2), (mouseY*len_S)-(win/2),win, win);
                        // Draw your 20x20 image at the mouse's location
                        g.drawImage(croppedImage, 0, 0, null); // Replace 'your20x20Image' with your image
                    }
                };
                mouseImageFrame.add(mouseImagePanel);






//                frame.setVisible(true);
//                mouseImageFrame.add(mouseImagePanel);
//                mouseImageFrame.setVisible(true);

                if (e.isControlDown()){
                    mouseImageFrame.setVisible(true);

                }


            }

        });

        frame.pack();
        frame.setVisible(true);


    }

    public static void main(String[] args) {
        ImageDisplayzz ren = new ImageDisplayzz();
        ren.showIms(args);
    }

}

