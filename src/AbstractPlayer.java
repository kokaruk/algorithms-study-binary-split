import java.io.IOException;
import java.util.*;

/**
 * Abstract player class
 * Both concrete classes have same implementation for
 * guess() and receiveAnswer() methods
 * and same instance variables, its only logical to have these implementations in an abstract class
 *
 * @author dimz
 * @since 17/5/18.
 */
abstract public class AbstractPlayer implements Player {
    // map of AttributeName, List of Attribute Values
    // The range of attributes that a player can guess, e.g, gender, eyeColor, etc.
    Map<String, List<String>> possibleAttributesToGuess;
    // map of Person Name as Key and Map of Attribute and Value pairs
    // All people in the game
    Map<String, Map<String, String>> guessCards;
    //
    private Map<String, Map<String, String>> chosenCard;

    public AbstractPlayer(String gameFilename, String chosenName) throws IOException {
        DataLoader dataLoader = DataLoader.getInstance(gameFilename);
        possibleAttributesToGuess = new HashMap<>(dataLoader.getAttributes());
        guessCards = new HashMap<>(dataLoader.getGuessCards());
        this.chosenCard = new HashMap<>();
        chosenCard.put(chosenName, new HashMap<>(guessCards.get(chosenName)));
    }


    public boolean answer(Guess currGuess) {
        return currGuess.getType() == Guess.GuessType.Attribute ? //if the opponent asks about att
                chosenCard.entrySet().iterator().next().getValue().get(currGuess.getAttribute()).equals(currGuess.getValue())
                : chosenCard.containsKey(currGuess.getValue()); //if the rival asks about person name
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {  //////   have this player won?
        if (currGuess.getType() == Guess.GuessType.Attribute) { // if it was guessing attribute
            Set<String> cardsToBeRemoved = new HashSet<>();
            for (Map.Entry<String, Map<String, String>> guessCard : guessCards.entrySet()) {
                /*
                eliminate all cards that don't have this attribute value
                if answer true and guess value doesn't equal to attribute value,

                eliminate cards that have such attribute value
                if answer false and value equal to attribute value
                 */
                if ((answer && !guessCard.getValue().get(currGuess.getAttribute()).equals(currGuess.getValue()))
                        || (!answer && guessCard.getValue().get(currGuess.getAttribute()).equals(currGuess.getValue()))) {
                    cardsToBeRemoved.add(guessCard.getKey());
                }
            }
             // guessCards.keySet().removeAll(cardsToBeRemoved);
            for(String cardName : cardsToBeRemoved){
                reduceRedundantAttributeValues(cardName);
            }
            return false;
        } else {  //if this player asks about person name
                return answer;    //guessed the correct person, the game is ended.
        }
    } // end of receiveAnswer()

    void reduceRedundantAttributeValues(String cardName){
        // check for redundant attribute,value pairs as this card can be the las of attribute value
        // but attributes collection to guess still may contain it
        Map<String, String> guessingCardAttributes = new HashMap<>(guessCards.get(cardName));
        guessCards.remove(cardName); // remove card from collection

        // iterate over attributes
        for (Map.Entry<String, String> attributeValue : guessingCardAttributes.entrySet()) {
            boolean attribPresentInCollection = false;
            // iterate over cards
            for (Map.Entry<String, Map<String, String>> card : guessCards.entrySet()) {
                // if cards attribute : value still present in cards collection, exit loop
                attribPresentInCollection = card.getValue().get(attributeValue.getKey()).equals(attributeValue.getValue());
                if (attribPresentInCollection) break;
            }
            if (!attribPresentInCollection) { // if this attribute value no longer in use, remove it from collection
                List<String> attributeValues = new ArrayList<>(possibleAttributesToGuess.get(attributeValue.getKey()));
                attributeValues.remove(attributeValue.getValue());
                if (attributeValues.isEmpty()) { //if list of values becomes empty
                    possibleAttributesToGuess.remove(attributeValue.getKey()); // remove this attribute from attributes list
                } else {
                    possibleAttributesToGuess.replace(attributeValue.getKey(), attributeValues);
                }
            }
        }
    }

}
