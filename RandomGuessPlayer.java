import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

    private ArrayList<Attributes> possibleAttributesToGuess;            //The range of attributes that a player can guess, e.g, gender, eyeColor, etc.
    private Person chosenPerson;
    private ArrayList<Person> people;                                   //All people in the game

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
        File config = new File(gameFilename);
        this.chosenPerson = new Person(chosenName, gameFilename);
        this.possibleAttributesToGuess = new ArrayList<>();
        this.people = new ArrayList<>();

        try (Scanner input = new Scanner(config)) {
            String line = " ";
            while (line.length() != 0)                                         //read until the end of attribute section before meeting the first person's name
                                                                                //while storing each attribute and its values
            {
                line = input.nextLine();

                if (line.length() == 0)                                        //meet the empty line which the next line will be P1 name
                    break;

                StringTokenizer st = new StringTokenizer(line);
                String attName = st.nextToken();                                                //e.g., gender, eyeColor, etc.
                possibleAttributesToGuess.add(new Attributes(attName, gameFilename));           //e.g. gender male female
            }

            while (input.hasNextLine())                                         //storing a name and attributes for every person in the game in people ArrayList
            {
                line = input.nextLine();
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() == 1)
                    people.add(new Person(st.nextToken(), gameFilename));
            }

        }
    } // end of RandomGuessPlayer() constructor

    public void print()                                                                          //this is just for manual testing
    {
        System.out.println("The chosenPerson person: " + chosenPerson.getPersonName());
        chosenPerson.printAttributes();
        System.out.println();
        System.out.println("The guess range: ");
        for (Attributes a : possibleAttributesToGuess)
            a.printValues();
        for (Person b : people)
            System.out.println(b.getPersonName());
    }

    public Guess guess() {
        Random myRandm = new Random();
        int randomType = myRandm.nextInt(1 + 1);                                      //if 0 the program will ask about Attribute, otherwise it will ask about Person's name

        if (people.size() == 1)                                                                          //if there is only one person left to ask, ask him/her
            return new Guess(Guess.GuessType.Person, "", people.get(0).getPersonName());

        if (randomType == 0) {                  //ask about attribute and a value
            String attName;
            String value;

            int randomAtt = myRandm.nextInt(possibleAttributesToGuess.size() );          //randomly pick attribute to ask e.g. eyeColor, glasses etc.

            while (possibleAttributesToGuess.get(randomAtt).getValues().size() == 0)     //if the random function picks the attribute that is asked every of its value
            {                                                                            //remove such att because we don't want it to be asked again
                possibleAttributesToGuess.remove(randomAtt);
                randomAtt = myRandm.nextInt(possibleAttributesToGuess.size() );        //re-randomly pick another attribute to ask again
            }

            attName = possibleAttributesToGuess.get(randomAtt).getName();
            value = possibleAttributesToGuess.get(randomAtt).getRandomValue();          //randomly ask a value
            return new Guess(Guess.GuessType.Attribute, attName, value);

        } else {                            //ask about a person's name
            String personName;

            int randomPerson = myRandm.nextInt(people.size() );
            personName = people.get(randomPerson).getPersonName();
            return new Guess(Guess.GuessType.Person, "", personName);
        }
    } // end of guess()


    public boolean answer(Guess currGuess) {
        if (currGuess.getType() == Guess.GuessType.Attribute) {                                               //if the opponent asks about att and he's correct, return true.
            return  (this.chosenPerson.getAttValue(currGuess.getAttribute()).compareTo(currGuess.getValue()) == 0);
        } else {
            return  (this.chosenPerson.getPersonName().compareTo(currGuess.getValue()) == 0);               //if the rival asks about person name and he's correct, return true.
        }
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        if (currGuess.getType() == Guess.GuessType.Attribute) {
            if (answer) {
                for (int i = 0; i < people.size(); i++) {
                    /* If the answer for the current-guess attribute is correct, then eliminate people that do not have such attribute value */

                    if (currGuess.getValue().compareTo(people.get(i).getAttValue(currGuess.getAttribute())) != 0)
                        people.remove(i);
                    possibleAttributesToGuess.remove(currGuess.getAttribute());
                }
                return false;
            } else    //wrong answer
            {
                for (int i = 0; i < people.size(); i++) {
                    /* If the answer for the current-guess attribute is wrong, then eliminate people that have such attribute value */

                    if (currGuess.getValue().compareTo(people.get(i).getAttValue(currGuess.getAttribute())) == 0)
                        people.remove(i);

                    for ( Attributes attribute : possibleAttributesToGuess)
                        if (attribute.getName().compareTo(currGuess.getAttribute()) == 0)
                            attribute.removeAsked(currGuess.getValue());
                }
                return false;
            }
        } else                                                                                //if this player asks about person name
        {
            if (answer)                                                                     //guessed the correct person, the game is ended.
                return true;
            else {
                people.remove(currGuess.getValue());                                        //remove the wrong person not to be asked again
                return false;
            }
        }

    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
