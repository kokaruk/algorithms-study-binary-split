import java.io.IOException;
import java.util.*;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer extends AbstractPlayer implements Player {
     /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *    Note you can handle IOException within the constructor and remove
     *    the "throws IOException" method specification, but make sure your
     *    implementation exits gracefully if an IOException is thrown.
     */
    public BinaryGuessPlayer(String gameFilename, String chosenName)
        throws IOException {
        super(gameFilename, chosenName);
    } // end of BinaryGuessPlayer()


    public Guess guess() {
        if (guessCards.size() == 1) {//if there is only one card left to ask, ask it
            return new Guess(Guess.GuessType.Person, "", guessCards.entrySet().iterator().next().getKey());
        } else {

            // make map of existing attributes and values and their counts
            Map<String, Map<String, Integer>> attributesWithCounter = new HashMap<>();
            // iterate over all possible attributes and init attributesWithCounter collection
            for (Map.Entry<String, List<String>> attributeValuePair : possibleAttributesToGuess.entrySet() ){
                // init new map entry with  attribute name as key, and empty map
                attributesWithCounter.put(attributeValuePair.getKey(), new HashMap<>());
                for (String attributeValueName : attributeValuePair.getValue()){
                    attributesWithCounter.get(attributeValuePair.getKey()).put(attributeValueName, 0);
                }
            }

            // iterate over cards and its attributes, update counters
            for (Map.Entry<String, Map<String, String>> guessCard : guessCards.entrySet()){
                for(Map.Entry<String,String> cardAttribute : guessCard.getValue().entrySet()){
                    if(attributesWithCounter.containsKey(cardAttribute.getKey())){
                        attributesWithCounter.get(cardAttribute.getKey()).put(cardAttribute.getValue(),
                                attributesWithCounter.get(cardAttribute.getKey()).get(cardAttribute.getValue()) + 1);
                    }
                }
            }
            // comparator to compare attributes counts
            Comparator<TupleAttributes<String,Integer>> byCount = Comparator.comparing(TupleAttributes::getCounter);
            // new max heap priority queue, with most frequent attribute on top of queue
            PriorityQueue<TupleAttributes<String,Integer>> attributesQueue = new PriorityQueue<>(byCount.reversed());
            // insert all attributes to the max heap
            for(Map.Entry<String, Map<String, Integer>> attributeWithValuesCounter : attributesWithCounter.entrySet()){
                // iterate over values counts
                for(Map.Entry<String, Integer> valueCounter : attributeWithValuesCounter.getValue().entrySet()){
                    attributesQueue.add(
                            new TupleAttributes<>(attributeWithValuesCounter.getKey(),
                                    valueCounter.getKey(),
                                    valueCounter.getValue()));
                }
            }
            TupleAttributes<String,Integer> attributeTuple;
            do{
               attributeTuple = attributesQueue.remove();
            } while (attributeTuple.getCounter() > Math.ceil(guessCards.size()/2.0));

            /////////// remove this attribute from collection of attributes
            List<String> attributeValues = new ArrayList<>(possibleAttributesToGuess.get(attributeTuple.attributeName));
            attributeValues.remove(attributeTuple.attributeValue);
            if (attributeValues.isEmpty()){
                possibleAttributesToGuess.remove(attributeTuple.attributeName);
            } else {
                possibleAttributesToGuess.replace(attributeTuple.attributeName, attributeValues);
            }
            return new Guess(Guess.GuessType.Attribute, attributeTuple.attributeName, attributeTuple.attributeValue);

        }
    } // end of guess()

    /**
     * inner class to store attributes
     * @param <T> Attribute Name, Attribute Value
     * @param <U> Counter of Attribute Name, Value occurrences
     */
    class TupleAttributes<T extends String, U extends Integer>{
        T attributeName;
        T attributeValue;
        U counter;

        public TupleAttributes(T attributeName, T attributeValue, U counter) {
            this.attributeName = attributeName;
            this.attributeValue = attributeValue;
            this.counter = counter;
        }

        public U getCounter() {
            return counter;
        }
    }

} // end of class BinaryGuessPlayer
