
import java.io.*;
import java.lang.Math;
import java.util.ArrayList;

public class foreground {
    public static void main(String[] args) {

        int delta = 1; // delta value for comparing pixel to code-word pixel ---257 codeword=1 256
                         // codeword=1
        // 240 codeword=37396 245 codeword=35447 255 codeword=31900 lamdaMax 2529824
        // 125 codeword=143712 lamdaMax 2529824
        // 155 codeword=276390 lamdaMax 2529824 235 codeword=39233 lamdaMax 2529824
        // 110 codeword 296574 lamdaMax 2529824
        // 170 codeword 253889 lamdaMax 220094735
        // 230 codeword 41042 lamdaMax 220094735
        // 220 codeword 43995 lamdaMax 7336491 divider=60 307frames
        // 100 codeword 253196 lamdaMax 220094735 divider=2 307frames--good
        // 110 codeword 296574 lamdaMax 220094735 divider=2 307frames
        // 108 codeword 266519 lamdaMax 220094735 divider=2 307frames
        // 104 codeword 247374 lamdaMax 220094735 divider=2 307frames
        // 101 codeword 245976 lamdaMax 220094735 divider=2 307frames

        // 100 codeword 8485 lamdaMax 7169198 divider=2 10frames
        // 255 codeword 820 lamdaMax 7169198 divider=2 10frames
        int NumberOfFrame = 307;
        // int NumberOfFrame = 10;
        // 307/2 frames (N/2 frame)
        // int lamdaMax = 153;
        // int lamdaMax = pixelCount/(2*NumberOfFrame);
        // int lamdaMax = pixelCount/2; //N=2 for 1 frames
        // int lamdaMax = pixelCount / 6; // N=2 for 10 frames
        // int lamdaMax = pixelCount / 2; // N=2 for 1 frames
        // int lamdaMax = pixelCount/60; //N=5 for 307 frames
        // int lamdaMax = pixelCount/174; //N=2 for 307 frames
        // int lamdaMax = pixelCount/154; //N=2 for 307 frames
        // double lamdaDivider = 174; // N=2 for 307 frames
        // double lamdaDivider = 6; // N=2 for 10 frames
        // double lamdaDivider = 174; // N=2 for 307 frames
        double lamdaDivider = 174; // N=2 for 307 frames

        // Code word array list
        CodeWords codeWordsRead = new CodeWords();

        /* Build code word array list from the BMP input frames (from Mathlab output) of the MOV file */
        int lamdaMax = buildCodeWord(codeWordsRead, NumberOfFrame, delta, lamdaDivider);

        /* Build the background, front ground BMP output file for each BMP input frame file based 
        on the code word array list done from the above step */
        buildBackFrontGndOutput(codeWordsRead, NumberOfFrame, delta, lamdaMax);

    } // the end of main

    /* Build code word array list from the BMP input frames (from Mathlab output) of the MOV file */
    public static int buildCodeWord(CodeWords codeWordsRead, int NumberOfFrame, int delta, double lamdaDivider) {

        int testCnt = 0;
        int pixelCount = 0;

        int count = 0; // byte count
        Pixel pixel; // pixel object class
        int pixel_xRed = 0, pixel_xGreen = 0, pixel_xBlue = 0; // RGB color values of a pixel

        while (testCnt < NumberOfFrame) { // loops to read MOV frame files
            // setup to read frame data file
            String inputFile = String.format("test_data/test%d.bmp", testCnt + 1);

            try (
                    // setuo to read stream of byte data from the BMP frame data file
                    InputStream inputStream = new FileInputStream(inputFile);) {
                int byteRead = -1;
                int pixelRead = 0;

                /* Aux_data is the BMP structure data(72 bytes at the begining) of the input BMP
                   frame data file */
                ArrayList<Integer> aux_data = new ArrayList<Integer>();

                pixelInfo pixelData = new pixelInfo();
  
                // read in pixel data
                while ((byteRead = inputStream.read()) != -1) { // loop to read byte data from the stream

                    if (count < 72) {
                        /* BMP structure information stored in the begining of the BMP frame data file
                           store BMP structure information in the aux_data array list for the BMP output
                           file using in the later on */
                        if (testCnt == 0)
                            aux_data.add(byteRead);

                    } else if ((pixelRead < 3) && (count > 39)) {
                        if (pixelRead == 0) { // read value Red of the current pixel
                            pixel_xRed = byteRead;
                        }
                        if (pixelRead == 1) { // read value Green of the current pixel
                            pixel_xGreen = byteRead;
                        }
                        // full pixel data received
                        if (pixelRead == 2) { // read value Blue of the current pixel
                            pixel_xBlue = byteRead;
                            // store R,G,B data into the pixel object and store the pixel object
                            // into the pixelData array list
                            pixel = new Pixel(pixel_xRed, pixel_xGreen, pixel_xBlue);
                            pixelData.addPixel(pixel); // save pixel into the pixelData array list for the BMP frame

                            // Not a BMP configuration date read, process pixel data according to the
                            // algorithm for codeword setup
                            if (count > 75) {
                                processPixel(pixel, count, codeWordsRead, delta);
                            }
                        }
                        // next pixel color read (R G B color)
                        pixelRead++;

                        if (pixelRead > 2) { // done with the current pixel
                            pixelRead = 0; // intialize for the next pixel color read (R G B color)
                            pixelCount++; // increase pixel count
                        }
                    }

                    count++; // increase byte count

                }
                // inputStream.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.printf("Done with BMP input frame file %d \n", testCnt + 1);
            testCnt++;

        }

        int lamdaMax = (int) (pixelCount / lamdaDivider); // N=2 for 10 frames
        System.out.printf("lamdaMax = %d \n", lamdaMax);
        System.out.printf("code word size : %d \n", codeWordsRead.getSize());

        // Algorithm, section III
        // Implement the section III--changing the codeword run length calculation
        for (Code codeWord : codeWordsRead.getCodeWords()) {
            codeWord.auxXl.runLength = Math.max(
                    codeWord.auxXl.runLength,
                    (count - codeWord.auxXl.lastAccess +
                            codeWord.auxXl.firstAccess - 1));
        }

        return lamdaMax;
    }

    /* Build the background, front ground BMP output file for each BMP input frame file based on the code word array list */
    public static void buildBackFrontGndOutput(CodeWords codeWordsRead, int NumberOfFrame, int delta, int lamdaMax) {

        int testCnt = 0;
        Pixel pixel; // pixel object class
        int pixel_xRed = 0, pixel_xGreen = 0, pixel_xBlue = 0; // RGB color values of a pixel

        /* For each pixel in the BMP frame input, determine if it is backgroung or forground pixel */
        while (testCnt < NumberOfFrame) { // begin to read the pixel from the input BMP frame file again for the second
                                          // time

            String input2File = String.format("test_data2/test%d.bmp", testCnt + 1);
            String outputFile = String.format("test_output/test%d_out.bmp", testCnt + 1);
            try (

                    InputStream input2Stream = new FileInputStream(input2File);
                    OutputStream outputStream = new FileOutputStream(outputFile);) {

                Pixel pixelMakeup = new Pixel(0, 0, 0);
                ArrayList<Integer> aux_data = new ArrayList<Integer>();
                pixelInfo pixelData = new pixelInfo();

                int byte2Read = -1;
                int cnt = 0; // byte count
                int pixelRead = 0;

                // get pixelData again for each BMP frame file
                while ((byte2Read = input2Stream.read()) != -1) {

                    if (cnt < 72) {
                        // BMP structure inforemation for the first file
                        aux_data.add(byte2Read);

                    } else if (pixelRead < 3) {
                        if (pixelRead == 0) { // read value Red of the current pixel
                            pixel_xRed = byte2Read;
                        }
                        if (pixelRead == 1) { // read value Green of the current pixel
                            pixel_xGreen = byte2Read;
                        }

                        // full pixel data received
                        if (pixelRead == 2) { // read value Blue of the current pixel
                            pixel_xBlue = byte2Read;
                            pixel = new Pixel(pixel_xRed, pixel_xGreen, pixel_xBlue);
                            pixelData.addPixel(pixel); // save pixel into the pixelData array list for the BMP frame
                        }

                        // next pixel color read (R G B color)
                        pixelRead++;

                        if (pixelRead > 2) { // done with the current pixel
                            pixelRead = 0; // intialize for the next pixel color read (R G B color)

                        }

                    }

                    cnt++; // increase byte count

                }

                System.out.printf("Done with 2nd BMP input frame file %d \n", testCnt + 1);
                System.out.println("Writing output file...");

                // Print out the BMP structural data at the beginning
                for (int i = 0; i < aux_data.size(); i++) {
                    outputStream.write(aux_data.get(i));
                }

                // setup pixel class object
                pixelMakeup = new Pixel(0, 0, 0);

                // read each pixel in the pixelData array list for this BMP frame
                for (Pixel pixelSaved : pixelData.getPixelLists()) {

                    /* Determine the current pixel is background/foregound by looking at the code word match
                     and conditions (N/2 for lambda) */
                    if (searchCodeWordForbackGnd(pixelSaved, codeWordsRead, lamdaMax, delta)) {

                        // background pixel ( matched)
                        pixelMakeup.xRed = 0;
                        pixelMakeup.xGreen = 0;
                        pixelMakeup.xBlue = 0;

                    } else {
                        // foreground pixel ( not matched)
                        pixelMakeup.xRed = 255;
                        pixelMakeup.xGreen = 255;
                        pixelMakeup.xBlue = 255;

                    }
                    // write data to output file (BMP format)
                    outputStream.write(pixelMakeup.xRed);
                    outputStream.write(pixelMakeup.xGreen);
                    outputStream.write(pixelMakeup.xBlue);
                }
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.printf("Done with output test file %d \n", testCnt + 1);
            testCnt++;
        }
    }

    /* if pixel data is not in the codeWordsRead array list, create a new code word to the codeWordsRead array list */
    public static void processPixel(Pixel pixel, int count, CodeWords codeWordsRead, int delta) {

        if (!searchCodeWord(pixel, count, codeWordsRead, delta)) {

            // Not in the code word list, add a new code word to the codeWordsRead array
            // list
            auxCode auxCodeRead = new auxCode(0.0, 0.0, 1, count - 1, count, count);
            auxCodeRead.brightMin = Math.sqrt(Math.pow(pixel.xRed, 2)
                    + Math.pow(pixel.xGreen, 2)
                    + Math.pow(pixel.xBlue, 2));
            auxCodeRead.brightMax = auxCodeRead.brightMin;

            Code codeRead = new Code(pixel, auxCodeRead);
            codeWordsRead.addCode(codeRead);

        }

    }

    /*search for the pixel data in the codeWordsRead array list. If match, update the codeword information
       for pixel color data average, intensity brightness(min/max), frequency, run length, last access. */
    public static boolean searchCodeWord(Pixel pixel, int count, CodeWords codeWordsRead, int delta) {

        for (Code codeWord : codeWordsRead.getCodeWords()) {

            // Match a code word
            if ((Math.abs(codeWord.CodeVl.xRed - pixel.xRed) < delta) &&
                    (Math.abs(codeWord.CodeVl.xGreen - pixel.xGreen) < delta) &&
                    (Math.abs(codeWord.CodeVl.xBlue - pixel.xBlue) < delta)) {

                // Calculate the average red, green, blue of the codeword
                codeWord.CodeVl.xRed = (pixel.xRed + (codeWord.CodeVl.xRed * codeWord.auxXl.frequency)) /
                        (codeWord.auxXl.frequency + 1);
                codeWord.CodeVl.xGreen = (pixel.xGreen + (codeWord.CodeVl.xGreen * codeWord.auxXl.frequency)) /
                        (codeWord.auxXl.frequency + 1);
                codeWord.CodeVl.xBlue = (pixel.xBlue + (codeWord.CodeVl.xBlue * codeWord.auxXl.frequency)) /
                        (codeWord.auxXl.frequency + 1);

                // pixel brightness intensity
                double pixelBrightness = Math.sqrt(Math.pow(pixel.xRed, 2)
                        + Math.pow(pixel.xGreen, 2)
                        + Math.pow(pixel.xBlue, 2));

                // Determine codeword minimum brightness / codeword maximum brightness
                if (pixelBrightness < codeWord.auxXl.brightMin)
                    codeWord.auxXl.brightMin = pixelBrightness;
                else if (pixelBrightness > codeWord.auxXl.brightMax)
                    codeWord.auxXl.brightMax = pixelBrightness;

                // increase the code word frequency
                codeWord.auxXl.frequency++;

                // Maximum run-length
                if ((count - codeWord.auxXl.lastAccess) > codeWord.auxXl.runLength)
                    codeWord.auxXl.runLength = count - codeWord.auxXl.lastAccess;

                // update last access count
                codeWord.auxXl.lastAccess = count;
                return true;
            }
        }

        return false;
    }

    /* search for the pixel data in the codeWordsRead array list that match conditions for background pixel (matched) */
    static boolean searchCodeWordForbackGnd(Pixel pixel, CodeWords codeWordsRead,
            int lamdaMax, int delta) {

        for (Code codeWord : codeWordsRead.getCodeWords()) {

            if (((Math.abs(codeWord.CodeVl.xRed - pixel.xRed) < delta) &&
                    (Math.abs(codeWord.CodeVl.xGreen - pixel.xGreen) < delta) &&
                    (Math.abs(codeWord.CodeVl.xBlue - pixel.xBlue) < delta) &&
                    // (pixelBrightness >= Ilow) &&
                    // (pixelBrightness <= Ihigh) &&
                    // ( (pixelBrightness < Ilow) ||
                    // (pixelBrightness > Ihigh)
                    // ) &&
                    // (codeWord.auxXl.frequency > 5000) &&
                    // (codeWord.auxXl.frequency > 600) &&
                    (codeWord.auxXl.runLength < lamdaMax))) {
                // System.out.println("Search matched");
                return true;
            }
        }

        return false;
    }

}