package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * The Shell class. This class renders a shell terminal in which the user
 * writes their input and the program runs accordingly.
 * @author Adam Shtrasner
 */
public class Shell {

    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String TERMINAL_PREFIX = ">>> ";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";

    // messages to user
    private static final String ILLEGAL_COMMAND_MESSAGE = "Illegal Command! Type again." +
            " Possible Legal commands:\n";
    private static final String LEGAL_COMMANDS = "exit/chars/render/console/res [up/down]/" +
            "[add/remove] [space/'letter'/'letter'-'letter'/all]";
    private static final String MAXIMAL_RESOLUTION_MESSAGE = "You're using the maximal resolution";
    private static final String MINIMAL_RESOLUTION_MESSAGE = "You're using the minimal resolution";

    // regex
    private static final String SINGLE_CHAR = "[ -~]";
    private static final String CHARACTER_RANGE = "[ -~]-[ -~]";

    // legal commands
    private static final String ADD_PREFIX = "add";
    private static final String REMOVE_PREFIX = "remove";
    private static final String CHAR_SET_PRINTING = "chars";
    private static final String ALL_CHARACTER = "all";
    private static final String SPACE_CHARACTER = "space";
    private static final String RESOLUTION_PREFIX = "res";
    private static final String INCREASE_RESOLUTION = "up";
    private static final String DECREASE_RESOLUTION = "down";
    private static final String CONSOLE_PREFIX = "console";
    private static final String RENDER_PREFIX = "render";
    private static final String CMD_EXIT = "exit";

    private final Set<Character> charSet = new HashSet<>();
    private final BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;

    /**
     * @param img the image to turn into an ascii representation
     */
    public Shell(Image img) {
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     * The main method of the class. This method gets the input from the user
     * and removes/adds characters to render the image with those characters
     * and increases/decreases resolution, according to that input.
     * If the input is "exit" - the program exits.
     */
    public void run() throws IllegalArgumentException {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print(TERMINAL_PREFIX);
            String cmd = scanner.nextLine().trim();
            String[] words = cmd.split("\\s+");

            // verify that there are no words
            // inserted after "exit"
            if (words.length > 1) {
                if (words[0].equals(CMD_EXIT)) {
                    throw new IllegalArgumentException();
                }
            }

            while (!words[0].equals(CMD_EXIT)) {
                if (!words[0].equals("")) {
                    String param = "";
                    if (words.length > 1) {
                        // all words of length greater than 2 are not allowed
                        if (words.length > 2) {
                            throw new IllegalArgumentException();
                        }
                        param = words[1];
                        switch (words[0]) {
                            case ADD_PREFIX:
                                addChars(param);
                                break;
                            case REMOVE_PREFIX:
                                removeChars(param);
                                break;
                            case RESOLUTION_PREFIX:
                                resChange(param);
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    } else {
                        switch (words[0]) {
                            case CHAR_SET_PRINTING:
                                showChars();
                                break;
                            case RENDER_PREFIX:
                                render();
                                break;
                            case CONSOLE_PREFIX:
                                output = new ConsoleAsciiOutput();
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                }
                System.out.print(TERMINAL_PREFIX);
                cmd = scanner.nextLine().trim();
                words = cmd.split("\\s+");
            }
        } catch(IllegalArgumentException illegalArgumentException) {
            System.out.println(ILLEGAL_COMMAND_MESSAGE + LEGAL_COMMANDS);
            run();
        }
    }

    /*
     Prints the current set of character.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /*
     Adds characters to the characters set.
     */
    private void addChars(String s) {
        char[] range = parseCharRange(s);

        // add all range to charSet
        if (range[0] == range[1]) {
            charSet.add(range[0]);
        }
        else {
            if (range[0] > range[1]) {
                Stream.iterate(range[1], c -> c <= range[0],
                        c -> (char)((int)c+1)).forEach(charSet::add);
            }
            else {
                Stream.iterate(range[0], c -> c <= range[1],
                        c -> (char)((int)c+1)).forEach(charSet::add);
            }
        }
    }

    /*
     Removes character from the characters set.
     */
    private void removeChars(String s) {
        char[] range = parseCharRange(s);

        // add all range to charSet
        if (range[0] == range[1]) {
            charSet.remove(range[0]);
        }
        else {
            if (range[0] > range[1]) {
                Stream.iterate(range[1], c -> c <= range[0],
                        c -> (char)((int)c+1)).forEach(charSet::add);
            }
            else {
                Stream.iterate(range[0], c -> c <= range[1],
                        c -> (char)((int)c+1)).forEach(charSet::add);
            }
        }
    }

    /*
     Parses the user's input into a two sized characters array.
     */
    private static char[] parseCharRange(String param) throws IllegalArgumentException {
        Matcher m;

        Pattern singleChar = Pattern.compile(SINGLE_CHAR);
        m = singleChar.matcher(param);
        if (m.matches()) {
            return new char[] {param.charAt(0), param.charAt(0)};
        }

        Pattern characterRange = Pattern.compile(CHARACTER_RANGE);
        m = characterRange.matcher(param);
        if (m.matches()) {
            return new char[] {param.charAt(0), param.charAt(2)};
        }

        if (param.equals(ALL_CHARACTER)) {
            return new char[] {' ', '~'};
        }

        if (param.equals(SPACE_CHARACTER)) {
            return new char[] {' ', ' '};
        }

        throw new IllegalArgumentException();
    }

    /*
     Changes the resolution of the image.
     */
    private void resChange(String s) throws IllegalArgumentException {
        if (s.equals(INCREASE_RESOLUTION)) {
            if (charsInRow * 2 > maxCharsInRow) {
                System.out.println(MAXIMAL_RESOLUTION_MESSAGE);
            }
            else {
                charsInRow *= 2;
            }
        }
        else if (s.equals(DECREASE_RESOLUTION)) {
            if (charsInRow / 2 < minCharsInRow) {
                System.out.println(MINIMAL_RESOLUTION_MESSAGE);
            }
            else {
                charsInRow /= 2;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /*
     renders the image. The default rendering is a html output.
     If at any time the input from the user was "console", the output
     is rendered to the console.
     */
    private void render() {
        if (charSet.size() != 0) {
            // construct the characters array from charSet
            Character[] chars = charSet.toArray(new Character[0]);
            output.output(charMatcher.chooseChars(charsInRow, chars));
        }
    }
}
