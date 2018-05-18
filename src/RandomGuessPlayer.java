import java.io.IOException;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer extends AbstractPlayer implements Player {

    /**
     * Loads the game configuration from gameFilename, and also store the chosenPerson
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName   Name of the chosenPerson person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *                     Note you can handle IOException within the constructor and remove
     *                     the "throws IOException" method specification, but make sure your
     *                     implementation exits gracefully if an IOException is thrown.
     */
    public RandomGuessPlayer(String gameFilename, String chosenName)
            throws IOException {
        super(gameFilename, chosenName);
    } // end of RandomGuessPlayer() constructor

    public Guess guess() {
        if (guessCards.size() == 1) { //if there is only one card left, ask it
            return new Guess(Guess.GuessType.Person, "", guessCards.entrySet().iterator().next().getKey());
        }
        Random myRandom = new Random(); // new random

        // new random guess type
        Guess.GuessType guessType = Guess.GuessType.values()[myRandom.nextInt(Guess.GuessType.values().length)];

        if (guessType == Guess.GuessType.Attribute) {           //ask about attribute and a value
            // get array of attribute names from map
            String[] attributeNames = possibleAttributesToGuess.keySet().toArray(new String[0]);
            //randomly pick attribute to ask e.g. eyeColor, glasses etc.
            String randomAttributeName = attributeNames[myRandom.nextInt(attributeNames.length)];      // random attribute name
            // needs to be a new array list, as map holds abstract list which doesn't support remove operation
            List<String> attributeValues = new ArrayList<>(possibleAttributesToGuess.get(randomAttributeName));
            String attributeValue = attributeValues.remove(myRandom.nextInt(attributeValues.size())); //randomly pick a value
            /////////// remove this attribute from collection of attributes
            if (attributeValues.isEmpty()) { //if list of values becomes empty
                possibleAttributesToGuess.remove(randomAttributeName); // remove this attribute from attributes list
            } else {
                possibleAttributesToGuess.replace(randomAttributeName, attributeValues);
            }
            return new Guess(guessType, randomAttributeName, attributeValue);

        } else {  //ask about a person's name
            String cardName;
            String[] cardNames = guessCards.keySet().toArray(new String[0]); // get card names from cards map
            cardName = cardNames[myRandom.nextInt(cardNames.length)];

            reduceRedundantAttributeValues(cardName);

            return new Guess(Guess.GuessType.Person, "", cardName);
        }

    } // end of guess()

} // end of class RandomGuessPlayer
