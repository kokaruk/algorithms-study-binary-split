import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author dimz
 * @since 15/5/18.
 * data reading class
 * reads a config file
 * implements a singleton pattern
 */
class DataLoader {


    private static String dataFileName;
    private static DataLoader instance;

    // map of AttributeName, List of Attribute Values
    private Map<String, List<String>> attributes;
    // map of Person Name as Key and Map of Attribute and Value pairs
    private Map<String, Map<String, String>> guessCards;

    //////   getters of data /////


    // private constructor
    private DataLoader() throws IOException {
        attributes = new LinkedHashMap<>();
        guessCards = new LinkedHashMap<>();
        loadData();
    }

    //lazy instance getter
    static DataLoader getInstance(String fileName) throws IOException {
        dataFileName = fileName;
        instance = new DataLoader();
        return instance;
    }

    public Map<String, List<String>> getAttributes() {
        return attributes;
    }

    public Map<String, Map<String, String>> getGuessCards() {
        return guessCards;
    }

    /**
     * load data from file
     */
    private void loadData() throws IOException {
        Path path = Paths.get(dataFileName);
        //make linked list, as it implements the queue interface
        LinkedList<String> lines = new LinkedList<>();
        // add lines from file to queue
        lines.addAll(Files.readAllLines(path));

        //init attributes from the stack
        initAttributes(lines);
        //init paying cards from the stack
        initGuessCards(lines);
    }


    /**
     * init all possible attributes
     *
     * @param lines - stack of all lines in config file
     */
    private void initAttributes(LinkedList<String> lines) {
        String line;
        while ((line = lines.remove()).length() != 0) {
            List<String> attributesString = Arrays.asList(line.split(" "));
            attributes.put(attributesString.get(0), attributesString.subList(1, attributesString.size()));
        }
    }

    /**
     * init guessing cards
     * firs line is always card name
     * then attribute <-> value pair
     * until line breaks, then recursively call self
     * until end of collection
     *
     * @param lines
     */
    private void initGuessCards(LinkedList<String> lines) {
        String name = lines.remove();
        Map<String, String> attributes = new LinkedHashMap<>();
        String line;
        while (!lines.isEmpty() && (line = lines.remove()).length() != 0) {
            String[] attributesPair = line.split(" ");
            attributes.put(attributesPair[0], attributesPair[1]);
        }
        guessCards.put(name, attributes);

        // if lines still have more cards data, lets recursively do it again
        if (!lines.isEmpty()) {
            initGuessCards(lines);
        }

    }


}
