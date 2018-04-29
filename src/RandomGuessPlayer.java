import java.io.*;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player
{
    private ArrayList<Attributes> guessPossibility;                                             //The range of attributes that a player can guess, e.g, gender, eyeColor, etc.
    private Person chosen;
    private ArrayList<String> people;
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
    public RandomGuessPlayer(String gameFilename, String chosenName)
        throws IOException
    {
        File config = new File(gameFilename);
        this.chosen = new Person(chosenName);
        this.guessPossibility = new ArrayList<>();
        this.people = new ArrayList<>();

        try(Scanner input = new Scanner(config))
        {
            String line = " ";
            while (line.length() != 0)                                         //read until the end of attribute section before meeting the person name
            {
                line = input.nextLine();

                if(line.length() == 0)
                    break;

                StringTokenizer st = new StringTokenizer(line);
                String attName = st.nextToken();                                //e.g., gender, eyeColor, etc.
                guessPossibility.add(new Attributes(attName));                  //e.g. gender male female
            }

            while (input.hasNextLine())                                         //read until the end of attribute section before meeting the person name
            {
                line = input.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                if(st.countTokens() == 1)
                    people.add(st.nextToken());
            }

        }
    } // end of RandomGuessPlayer()

    public void print()
    {
        System.out.println("The chosen person: " + chosen.getPersonName());
        chosen.printAttributes();
        System.out.println();
        System.out.println("The guess range: ");
        for(Attributes a: guessPossibility)
            a.printValues();
        for(String b: people)
            System.out.println(b);
    }

    public Guess guess()
    {
        int randomType = 0 + (int)(Math.random() * ((1 - 0) + 1));                                      //if 0 = Attribute, 1 = Person

        if(randomType == 0)
        {
            String attName;
            String value;

            int randomAtt = 0 + (int)(Math.random() * (((guessPossibility.size()-1) - 0) + 1));
            attName = guessPossibility.get(randomAtt).getName();
            value = guessPossibility.get(randomAtt).getRandomValue();
            return new Guess(Guess.GuessType.Attribute, attName, value);
        }
        else
        {
            String personName;

            int randomPerson = 0 + (int)(Math.random() * (((people.size()-1) - 0) + 1));
            personName = people.get(randomPerson);
            return new Guess(Guess.GuessType.Person, "", personName);
        }
    } // end of guess()


    public boolean answer(Guess currGuess) {

        // placeholder, replace
        return false;
    } // end of answer()


	public boolean receiveAnswer(Guess currGuess, boolean answer) {

        // placeholder, replace
        return true;
    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
