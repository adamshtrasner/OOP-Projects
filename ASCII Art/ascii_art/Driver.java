package ascii_art;

import image.Image;
import java.util.logging.Logger;

public class Driver {

    /**
     * The main mathod of ASCII art. Gets the image path as an argument and Runs Shell.
     * @param args a single argument representing a path to an image.
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " +
                    args[0]);
            return;
        }
        new Shell(img).run();
    }
}
