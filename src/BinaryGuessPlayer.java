import java.io.IOException;

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
            // init priority queue
        //    Queue<>
            return new Guess(Guess.GuessType.Person, "", "Placeholder");
        }
    } // end of guess()

    /**
     * inner class to store attributes
     * @param <T> Attribute Name, Attribute Value
     * @param <U> Counter of Attribute Name, Value occurrences
     */
    class TupleAttributes<T extends String, U extends Integer>{

    }

} // end of class BinaryGuessPlayer
