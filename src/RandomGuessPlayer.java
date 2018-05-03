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
    private ArrayList<Attributes> guessPossibility;                          //The range of attributes that a player can guess, e.g, gender, eyeColor, etc.
    private Person chosen;
    private ArrayList<Person> people;
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
        this.chosen = new Person(chosenName, gameFilename);
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
                String attName = st.nextToken();                                                //e.g., gender, eyeColor, etc.
                guessPossibility.add(new Attributes(attName, gameFilename));                  //e.g. gender male female
            }

            while (input.hasNextLine())                                         //read until the end of attribute section before meeting the person name
            {
                line = input.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                if(st.countTokens() == 1)
                    people.add(new Person(st.nextToken(), gameFilename));
            }

        }
    } // end of RandomGuessPlayer()

    public void print()                                                                          //this is just for manual testing
    {
        System.out.println("The chosen person: " + chosen.getPersonName());
        chosen.printAttributes();
        System.out.println();
        System.out.println("The guess range: ");
        for(Attributes a: guessPossibility)
            a.printValues();
        for(Person b: people)
            System.out.println(b.getPersonName());
    }

    public Guess guess()
    {
        Random myRandm = new Random();
        int randomType = myRandm.nextInt(1);                                      //if 0 = Attribute, 1 = Person

        if(people.size() == 1)                                                                          //if there is only one person left to ask
            return new Guess(Guess.GuessType.Person, "", people.get(0).getPersonName());

        if(randomType == 0)
        {
            String attName;
            String value;

            int randomAtt = myRandm.nextInt(guessPossibility.size());          //randomly pick attribute to ask e.g. eyeColor, glasses etc.

            while(guessPossibility.get(randomAtt).getValues().size() == 0)                              //if the random function picks the attribute that is asked every of its value
            {                                                                                           //remove such att because we don't want it to be asked anymore
                guessPossibility.remove(randomAtt);
                randomAtt = myRandm.nextInt(guessPossibility.size());        //re-randomly pick again
            }

            attName = guessPossibility.get(randomAtt).getName();
            value = guessPossibility.get(randomAtt).getRandomValue();
            return new Guess(Guess.GuessType.Attribute, attName, value);
        }
        else
        {
            String personName;

            int randomPerson = myRandm.nextInt(1);
            personName = people.get(randomPerson).getPersonName();
            return new Guess(Guess.GuessType.Person, "", personName);
        }
    } // end of guess()


    public boolean answer(Guess currGuess)
    {
        if(currGuess.getType()== Guess.GuessType.Attribute) {                                               //if the opponent asks about att and he's correct
            if(this.chosen.getAttValue(currGuess.getAttribute()).compareTo(currGuess.getValue()) == 0)
                return true;
            else
                return false;
        }
        else {
            if (this.chosen.getPersonName().compareTo(currGuess.getValue()) == 0)                           //if the rival asks about person name and he's correct
                return true;
            else
                return false;
        }
    } // end of answer()


	public boolean receiveAnswer(Guess currGuess, boolean answer)
    {
        if(currGuess.getType()== Guess.GuessType.Attribute)
        {
            if(answer)
            {
                for(int i = 0; i < people.size(); i++)
                {
                    /* If the answer for the current-guess attribute is correct, then eliminate people that do not have such attribute value */

                    if(currGuess.getValue().compareTo(people.get(i).getAttValue(currGuess.getAttribute())) != 0)
                        people.remove(i);
                    guessPossibility.remove(currGuess.getAttribute());
                }
                return false;
            }
            else    //wrong answer
            {
                for(int i = 0; i < people.size(); i++)
                {
                    /* If the answer for the current-guess attribute is wrong, then eliminate people that have such attribute value */

                    if(currGuess.getValue().compareTo(people.get(i).getAttValue(currGuess.getAttribute())) == 0)
                        people.remove(i);

                    for(int j = 0; j < guessPossibility.size(); j++)
                        if(guessPossibility.get(j).getName().compareTo(currGuess.getAttribute()) == 0)
                            guessPossibility.get(j).removeAsked(currGuess.getValue());
                }
                return false;
            }
        }
        else                                                                                //if this player asks about person name
        {
            if(answer)                                                                     //guessed the correct person
                return true;
            else
            {
                people.remove(currGuess.getValue());                                        //remove the wrong person not to be ask again
                return false;
            }
        }

    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
