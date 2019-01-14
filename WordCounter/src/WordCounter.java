import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
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

    /**
     *
     */
    public static void removePuncuation(Sequence<String> listOfWords) {
        for (int i = 0; i < listOfWords.length(); i++) {
            String testVal = listOfWords.entry(i);
            if (testVal.equals(",")) {
                listOfWords.remove(i);
            }

            if (testVal.contains(".")) {
                listOfWords.remove(i);
                listOfWords.add(i, testVal.replace(".", ""));
            } else if (testVal.contains(",")) {
                listOfWords.remove(i);
                listOfWords.add(i, testVal.replace(",", ""));
            }
        }
    }

    /**
     *
     */
    public static void createListOfWords(Sequence<String> listOfWords,
            String lineOfTxt) {
        int x = 0;
        for (int i = lineOfTxt.indexOf(' '), j = 0; i != -1;) {
            listOfWords.add(x, lineOfTxt.substring(j, i));
            j = i + 1;
            i = lineOfTxt.indexOf(' ', j);
            x++;
        }
        String test = lineOfTxt.substring(lineOfTxt.lastIndexOf(' ') + 1,
                lineOfTxt.length());
        listOfWords.add(x, test);
        x = 0;
    }

    /**
     *
     *
     */
    public static void parseFile(Map<String, Integer> data, SimpleReader in) {
        while (!in.atEOS()) {
            Sequence<String> listOfWords = new Sequence1L<>();
            String lineOfTxt = in.nextLine();
            lineOfTxt = lineOfTxt.replace("   ", "");
            lineOfTxt = lineOfTxt.replace("--", " ");
            lineOfTxt = lineOfTxt.replace("-", " ");

            createListOfWords(listOfWords, lineOfTxt);
            removePuncuation(listOfWords);

            for (int i = 0; i < listOfWords.length(); i++) {
                if (!data.hasKey(listOfWords.entry(i))) {
                    data.add(listOfWords.entry(i), 0);
                }
            }

            for (int i = 0; i < listOfWords.length(); i++) {
                int count = data.value(listOfWords.entry(i));
                if (data.hasKey(listOfWords.entry(i))) {
                    data.replaceValue(listOfWords.entry(i), count + 1);
                }
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
        String inputName = "data/gettysburg.txt";//in.nextLine();
        SimpleReader fileIn = new SimpleReader1L(inputName);

        /*
         * Reads in the name of the output folder
         */

        out.println("Enter The Location & Name of Output File: ");
        String folder = "data/gettysburg.html";//in.nextLine();

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

        SimpleWriter index = new SimpleWriter1L(folder);
        createHeader(index, inputName);

        /*
         * Gets the words & puts them into the map
         */

        parseFile(data, fileIn);
        data.remove("");
        for (Map.Pair<String, Integer> x : data) {
            out.println(x);
        }

        in.close();
        out.close();
        fileIn.close();
    }
}
