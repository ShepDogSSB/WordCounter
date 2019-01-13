import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program counts word occurrences in a given input file and outputs an
 * HTML document with a table of the words and counts listed in alphabetical
 * order.
 *
 * @author Nicholas Shepard - Project #1 - 1/12/19
 *
 */
public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    /**
     * Creates the header output html file.
     *
     * @param index
     *            The html to output the header to
     * @param inputName
     *            The name/location/file extension of the input file
     */
    public static void createHeader(SimpleWriter index, String inputName) {

        /*
         * Makes the document a valid html.
         */

        index.println("<!DOCTYPE html>");

        /*
         * prints out the leading information for the "home page".
         */

        index.println("<html>\n<head>\n<title>Words Counted in " + inputName
                + "</title></head>\n<body>" + "<h2>Words Counted in "
                + inputName + "</h2>" + "<hr>");
    }

    public static void getWords(Map<String, Integer> data, SimpleReader in) {

        /*
         * While we can still parse through the input
         */
        while (!in.atEOS()) {
            /*
             * We test to see if the next line has a space in it and if the line
             * is an empty string, if it doesn't have either, then we enqueue it
             * to the words queue.
             */
            String testVal = in.nextLine();
            if (!(testVal.contains(" ") && testVal.isEmpty())) {
                words.enqueue(testVal);
            }
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * This map will hold each unique word, and the number of times that
         * word appears in the given .txt file.
         */

        Map<String, Integer> data = new Map1L<>();

        /*
         * Reads in the text file to use as the input.
         */

        out.println("Enter The Location & Name of Input File: ");
        String inputName = in.nextLine();
        SimpleReader fileIn = new SimpleReader1L(inputName);

        /*
         * Reads in the name of the output folder
         */

        out.println("Enter The Name of the Output Folder: ");
        String folder = in.nextLine();

        /*
         * Makes a copy of the input file string, removes the ".txt" file
         * extension & the beginning folder directory.
         */

        String fileName = String.valueOf(inputName);
        fileName = fileName.replace(".txt", "");
        fileName = fileName.replace("data/", "");

        /*
         * Creates the header for the output html page, and the html page
         * itself.
         */

        SimpleWriter index = new SimpleWriter1L(
                folder + "/" + fileName + ".html");
        createHeader(index, inputName);

        /*
         * Gets the words & puts them into the map
         */

        getWords(data, fileIn);

        in.close();
        out.close();
        fileIn.close();
    }
}
