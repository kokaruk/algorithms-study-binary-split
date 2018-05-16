import java.io.IOException;
import java.util.*;


/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

    // map of AttributeName, List of Attribute Values
    //The range of attributes that a player can guess, e.g, gender, eyeColor, etc.
    private Map<String, List<String>> possibleAttributesToGuess;
    // map of Person Name as Key and Map of Attribute and Value pairs
    //All people in the game
    private Map<String, Map<String, String>> guessCards;
    //
    private String chosenName;


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
        this.chosenName = chosenName;
        DataLoader dataLoader = DataLoader.getInstance(gameFilename);
        possibleAttributesToGuess = new HashMap<>(dataLoader.getAttributes());
        guessCards = new HashMap<>(dataLoader.getGuessCards());
    } // end of RandomGuessPlayer() constructor

    public Guess guess() {
        if (guessCards.size() == 1) //if there is only one card left to ask, ask it
            return new Guess(Guess.GuessType.Person, "", guessCards.entrySet().iterator().next().getKey());

        Random myRandom = new Random(); // new random

        // new random guess type
        Guess.GuessType guessType = Guess.GuessType.values()[myRandom.nextInt(Guess.GuessType.values().length)];

        if (guessType == Guess.GuessType.Attribute) {                  //ask about attribute and a value
            // get array of attribute names from map
            String[] attributeNames = possibleAttributesToGuess.keySet().toArray(new String[0]);

            //randomly pick attribute to ask e.g. eyeColor, glasses etc.
            String randomAttributeName = attributeNames[myRandom.nextInt(attributeNames.length)];      // random attribute name
            // needs to be a new array list, as map holds abstract list which doesn't support remove operation
            List<String> attributeValues = new ArrayList<>(possibleAttributesToGuess.get(randomAttributeName));
            String attributeValue = attributeValues.get(myRandom.nextInt(attributeValues.size())); //randomly pick a value
            attributeValues.remove(myRandom.nextInt(attributeValues.size()));
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
            guessCards.remove(cardName); // remove card from collection
            return new Guess(Guess.GuessType.Person, "", cardName);
        }
    } // end of guess()

    public boolean answer(Guess currGuess) {
        try {
            boolean bool = currGuess.getType() == Guess.GuessType.Attribute ? //if the opponent asks about att
                    guessCards.get(chosenName).get(currGuess.getAttribute()).equals(currGuess.getValue())
                    : chosenName.equals(currGuess.getValue()); //if the rival asks about person name
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return currGuess.getType() == Guess.GuessType.Attribute ? //if the opponent asks about att
                guessCards.get(chosenName).get(currGuess.getAttribute()).equals(currGuess.getValue())
                : chosenName.equals(currGuess.getValue()); //if the rival asks about person name
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {  //////   have I finished?
        if (currGuess.getType() == Guess.GuessType.Attribute) { // if was guessing attribute
            Set<String> cardsToBeRemoved = new HashSet<>();
            for (Map.Entry<String, Map<String,String>> guessCard : guessCards.entrySet()) {
                /*
                eliminate all cards that don't have this attribute value
                if answer true and guess value doesn't equal to attribute value,
                 */
                if( answer && !guessCard.getValue().get(currGuess.getAttribute()).equals(currGuess.getValue()) ){
                    cardsToBeRemoved.add(guessCard.getKey());
                }
                /*
                eliminate cards that have such attribute value
                if answer false and value doesn't equal to attribute value
                 */
                if(!answer && guessCard.getValue().get(currGuess.getAttribute()).equals(currGuess.getValue())){
                    cardsToBeRemoved.add(guessCard.getKey());
                }

            }
            guessCards.keySet().removeAll(cardsToBeRemoved);
        } else {  //if this player asks about person name
            if (answer)                                                                     //guessed the correct person, the game is ended.
                return true;
            else {
                guessCards.remove(currGuess.getValue());                                        //remove the wrong person not to be asked again
            }
        }
        return false;
    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
