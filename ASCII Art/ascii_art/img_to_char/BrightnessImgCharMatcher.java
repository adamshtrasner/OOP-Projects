package ascii_art.img_to_char;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import image.Image;

import java.awt.*;

/**
 * This class matches image brightnesses to the corresponding ascii brightnesses.
 * @author adam Shtrasner
 */
public class BrightnessImgCharMatcher {

    private static final int NUM_PIXELS = 16;
    private static final int NUM_GRAY_LEVELS = 255;

    private final Image img;
    private final String font;

    private final Map<Character, Double> charToBrightness = new HashMap<>();
    private final HashMap<Image, Double> cache = new HashMap<>();


    public BrightnessImgCharMatcher(Image img, String font) {
        this.img = img;
        this.font = font;
    }

    /**
     * @param numCharsInRow Number of characters we'll draw in a row
     * @param charSet a character array with which we draw the image with
     * @return a 2D array that represents an image
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        double[] normalizedBrightnessSet = convertCharToBrightness(charSet);
        return convertImageToAscii(numCharsInRow, normalizedBrightnessSet, charSet.length);
    }

    /*
    This function converts the image to ascii according to numCharsInRow -
    number of characters we wish the image to have, and each sum image corresponds to
    the ascii letter which has the minimal distance between its brightness and the average
    brightness of the current sub image.
     */
    private char[][] convertImageToAscii(int numCharsInRow,
                                         double[] normalizedBrightnessSet,
                                         int charSetLength) {
        int pixels = img.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[img.getHeight()/pixels][img.getWidth()/pixels];
        int n = 0, m = 0;
        for(Image subImage : img.squareSubImagesOfSize(pixels)) {
            if (m == asciiArt[0].length) {
                m = 0;
                n++;
            }
            double avgPixel;
            if (cache.containsKey(subImage)) {
                avgPixel = cache.get(subImage);
            }
            else {
                // calculating average pixel of the sub image
                avgPixel = calculateAveragePixel(subImage);
                cache.put(subImage, avgPixel);
            }
            // calculating the brightness that has the minimal distance
            // between its brightness and the average pixel
            int i = 0, minDistanceIdx = 0;
            double distance = Math.abs(avgPixel - normalizedBrightnessSet[0]);
            while (i < charSetLength - 1) {
                double newDistance = Math.abs(avgPixel - normalizedBrightnessSet[i + 1]);
                if (newDistance < distance) {
                    distance = newDistance;
                    minDistanceIdx = i + 1;
                }
                i++;
            }
            asciiArt[n][m] = getKey(charToBrightness, normalizedBrightnessSet[minDistanceIdx]);
            m++;
        }
        return asciiArt;
    }

    /*
    Gets the key of the value from a given map according to some value.
     */
    private static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /*
    This function converts a 1D array of character into a corresponding 1D array
    of brightnesses.
     */
    private double[] convertCharToBrightness(Character[] charSet) {
        double[] brightnessSet = new double[charSet.length];

        // calculate number of white pixels in each character
        // and divide by total number of gray levels
        for (int i = 0; i < charSet.length; i++) {
            brightnessSet[i] = countWhitePixels(CharRenderer.getImg(charSet[i], NUM_PIXELS, font)) /
                    NUM_GRAY_LEVELS;
        }

        double min = findMin(brightnessSet);
        double max = findMax(brightnessSet);

        // return normalized brightness array
        return normalizeBrightness(charSet,
                brightnessSet,
                min,
                max);
    }

    /*
    Finds maximum value of an array.
     */
    private double findMax(double[] brightnessSet) {
        double max = brightnessSet[0];
        for (int i = 1; i < brightnessSet.length; i++) {
            if (brightnessSet[i] > max) {
                max = brightnessSet[i];
            }
        }
        return max;
    }

    /*
    Finds minimum value of an array.
     */
    private double findMin(double[] brightnessSet) {
        double min = brightnessSet[0];
        for (int i = 1; i < brightnessSet.length; i++) {
            if (brightnessSet[i] < min) {
                min = brightnessSet[i];
            }
        }
        return min;
    }

    /*
    This function counts the number of white pixels in a boolean image
     */
    private double countWhitePixels(boolean[][] cs) {
        double counter = 0;
        for (boolean[] c : cs) {
            for (int j = 0; j < cs[0].length; j++) {
                if (c[j]) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /*
    This function normalizes the brightness set.
     */
    private double[] normalizeBrightness(Character[] charSet,
                                         double[] brightnessSet,
                                         double min,
                                         double max) {
        for (int i = 0; i < charSet.length; i++) {
            brightnessSet[i] = (brightnessSet[i] - min) / (max - min);
            charToBrightness.put(charSet[i], brightnessSet[i]);
        }
        return brightnessSet;
    }

    /*
    This function calculates the average pixel of a given image
     */
    private double calculateAveragePixel(Image image) {
        double sum = 0;
        for (Color pixel : image.pixels()) {
            sum += (pixel.getRed() * 0.2126 +
                    pixel.getGreen() * 0.7152 +
                    pixel.getBlue() * 0.0722);
        }
        return (sum / (image.getHeight() * image.getWidth())) / 255;
    }
}