import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
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
     * @requires index is opened.
     * @ensures The proper header for an html is created.
     */
    public static void createHeader(SimpleWriter index, String inputName) {

        /*
         * Makes the document a valid html.
         */

        index.println("<!DOCTYPE html>");

        /*
         * prints out the leading information for the html page.
         */

        index.println("<html>\n<head>\n<title>Words Counted in " + inputName
                + "</title></head>\n<body>" + "<h2>Words Counted in "
                + inputName + "</h2>" + "<hr>");
    }

    /**
     * Removes all the punctuation tied to a word.
     *
     * @param listOfWords
     *            A list of words obtained from a line in the input text stream.
     * @requires |listOfWords| > 0;
     * @ensures All the words in {@code listOfWords} have no punctuation and
     *          there are no punctuation element in {@code listOfWords}
     */
    public static void removePuncuation(Sequence<String> listOfWords) {
        /*
         * This for loop goes through each element in list of words.
         */
        for (int i = 0; i < listOfWords.length(); i++) {
            /*
             * Each time this loop iterates, it assigns testVal to a word, and
             * first checks to see if its just a comma, and if it is, its
             * removed from listOfWords.
             */

            String testVal = listOfWords.entry(i);
            if (testVal.equals(",")) {
                listOfWords.remove(i);
            }

            /*
             * After the statements above, we then check to see if testVal has a
             * period or a comma attached to it, and if there is we remove
             * testVal from listOfWords, and put it back in at the same position
             * & replace the comma/period with an empty string.
             */
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
     * Picks each word from a given string and puts each word into a Sequence.
     *
     * @param listOfWords
     *            A list of words obtained from a line in the input text stream.
     * @param lineOfTxt
     *            A line from the input text stream.
     * @requires |lineOfText| > 0 && |listOfWords| == 0
     * @ensures {@code listOfWords} contains all the words from
     *          {@code lineOfTxt}
     */
    public static void createListOfWords(Sequence<String> listOfWords,
            String lineOfTxt) {
        // x represents a position in the Sequence
        int x = 0;
        /*
         * This for loop finds the first instance of a space, then pulls out the
         * word between j and the index of the space, i and add it to the
         * Sequence at position x. After that, we set j to go to the next word
         * and i to go to the next space, and we increment x by 1. This repeats
         * until we don't find a space in the string.
         */
        for (int i = lineOfTxt.indexOf(' '), j = 0; i != -1;) {
            listOfWords.add(x, lineOfTxt.substring(j, i));
            j = i + 1;
            i = lineOfTxt.indexOf(' ', j);
            x++;
        }
        /*
         * The for-loop above misses the last word, so here we set lastWord to
         * the substring of the first character of the lastWord to the length of
         * the whole string then add that to the sequence.
         */
        String lastWord = lineOfTxt.substring(lineOfTxt.lastIndexOf(' ') + 1,
                lineOfTxt.length());
        listOfWords.add(x, lastWord);
    }

    /**
     * Goes through the entire file and fills the data Map.
     *
     * @param data
     *            The map containing the words and the count for those words.
     * @param in
     *            The input file stream.
     * @requires in.isOpen
     * @ensures data contains all the words found in the input stream and and
     *          how many times that word appears in the input stream.
     */
    public static void parseFile(Map<String, Integer> data, SimpleReader in) {
        /*
         * While there are still lines in the file.
         */
        while (!in.atEOS()) {
            /*
             * Creates a Sequence that represents a list of words gotten from
             * the String lineOfTxt. Then we assign lineOfTxt to the next line
             * in the input stream, remove the tab & replace any separators w/ a
             * space.
             */
            Sequence<String> listOfWords = new Sequence1L<>();
            String lineOfTxt = in.nextLine();
            lineOfTxt = lineOfTxt.replace("   ", "");
            lineOfTxt = lineOfTxt.replace("--", " ");
            lineOfTxt = lineOfTxt.replace("-", " ");

            /*
             * These methods fill listOfWords with words gotten from linesOfTxt
             * and removes any punctuation connected to the words.
             */
            createListOfWords(listOfWords, lineOfTxt);
            removePuncuation(listOfWords);

            /*
             * This for loop goes through the list of words, checks data if
             * there exists a key in the the loop that represents the word that
             * the loop is currently trying to add, and if not, adds a pair to
             * the map with a key that is the word, and a value of 0.
             */
            for (int i = 0; i < listOfWords.length(); i++) {
                if (!data.hasKey(listOfWords.entry(i))) {
                    data.add(listOfWords.entry(i), 0);
                }
            }

            /*
             * This for loop goes through the list of words, assigned a count
             * variable to the value of a given key, where the value is the
             * number of times that word has appeared in the input text so far.
             * We check data to see if the word we are at is already in the Map
             * and if it is, we add 1 to count.
             */
            for (int i = 0; i < listOfWords.length(); i++) {
                int count = data.value(listOfWords.entry(i));
                if (data.hasKey(listOfWords.entry(i))) {
                    data.replaceValue(listOfWords.entry(i), count + 1);
                }
            }
        }
        // Removes the useless Map Pair for an empty string.
        data.remove("");
    }

    /**
     * Creates the table for the html page.
     *
     * @param data
     *            The map containing the words and the count for those words.
     * @param out
     *            the html to output the table to.
     * @param sortedWords
     *            A queue of the words in data, but sorted alphabetically.
     * @requires |sortedWords| > 0 && out.isOpen
     * @ensures The html page has a correctly formatted table
     */
    public static void createTable(Map<String, Integer> data, SimpleWriter out,
            Queue<String> sortedWords) {

        /*
         * Prints the table headers and the border.
         */

        out.println(
                "<table border=\"1\"><tr><th>Words</th><th>Counts</th></tr>");

        /*
         * While there are still words in the queue, we go through each pair in
         * data. When the pair.key() is the same as the first element in the
         * queue (which is in alphabetical order), we add that pair's key and
         * value to the table. After that we remove the first element in the
         * queue.
         */

        while (sortedWords.length() > 0) {
            for (Map.Pair<String, Integer> pair : data) {
                if (pair.key().equals(sortedWords.front())) {
                    out.println("<tr><td>" + pair.key() + "</td><td>"
                            + pair.value() + "</td></tr>");
                }
            }
            sortedWords.dequeue();
        }

        /*
         * Closing html tags.
         */

        out.println("</table></body></html>");
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

        out.println("Enter The Location & Name of Output File: ");
        String folder = in.nextLine();

        /*
         * Creates the header for the output html page, and the html page
         * itself.
         */

        SimpleWriter index = new SimpleWriter1L(folder);
        createHeader(index, inputName);

        /*
         * Parses the input file, and puts the appropriate data into a Map.
         */

        parseFile(data, fileIn);

        /*
         * Creates a queue with all the words in the Map and sorts them into
         * alphabetical order, to be used in the createTable method.
         */

        Queue<String> sortedWords = new Queue1L<>();
        for (Map.Pair<String, Integer> pair : data) {
            sortedWords.enqueue(pair.key());
        }
        sortedWords.sort(String.CASE_INSENSITIVE_ORDER);

        /*
         * Creates the html page table.
         */

        createTable(data, index, sortedWords);

        /*
         * Closes I/O Streams
         */

        in.close();
        out.close();
        fileIn.close();
    }
}
