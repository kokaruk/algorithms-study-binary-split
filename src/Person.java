import java.util.*;
import java.io.*;

public class Person
{
    /**
     * This class is used for storing a person's name and his/her attributes so that the player classes can apply it for guessing and referencing.
     * @config represents a configuration file that this class gathers the data of a specific person.
     * @atts maps the person's attributes with their names and values.
     * @name stores a name of the specified person.
     */
    private File config;
    private HashMap<String,String> atts = new HashMap<>();
    private String name;

    /* Specifying a person's name existing in the configuration file */
    public Person(String name, String config) throws IOException
    {
        this.name = name;

        this.config = new File(config);

        try(Scanner input = new Scanner(this.config))                   //Read the file to search and store the person's attributes
        {
            String line = " ";
            int lineNumb = 0;                                           //To count the number of the attributes' names this class will collect

            while (line.length() != 0)
            /* Skip the lines until the end of attributes' section (the group of attributes used for people
               in guessWho game before meeting the first person's name).
               e.g. eyeColor red green blue
                    hairColor red green blue
                    ..
                    ..
                                            <-- skip until meet this line
                    P1
                    eyeColor red
                    ..
                    .. */
            {
                line = input.nextLine();

                if(line.length() != 0)                                  //an empty line which is the end of attribute lines so the next line will be the first person's name
                    lineNumb++;                                         //And, count the number of attributes per person
            }

            boolean foundName = false;

            while (!foundName)                                          //Finding the specified person in the config file
            {
                if(input.next().compareTo(name) == 0) {
                    foundName = true;
                }
            }

           for(int i = 0; i < lineNumb; i++)                            //Adding the person attributes referenced by the number of lines the program counted
                                                                        //e.g. if there are six attributes per person, the lineNumb = 6;
           {
                String attName = input.next();                          //Read the name of an attribute
                String value = input.next();                            //Read the value of the attribute
                atts.put(attName,value);                                //Mapping them into atts
            }
        }
    }

    public String getPersonName()
    {
        return this.name;
    }

    /* Getting a value of a specified attribute's name */
    public String getAttValue(String attName)
    {
        String value = atts.get(attName);
        return value;
    }

    /* This method is just used for checking the storing process of this class */
    public void printAttributes()
    {
        for (Map.Entry<String,String> mentry : atts.entrySet()) {                                   // iterate amd print key vale pairs from attributes map
            System.out.println("Key: " + mentry.getKey() + " \nVal: " + mentry.getValue());
        }
    }

    public void removeAttribute(String key)
    {
        atts.remove(key);
    }
}
