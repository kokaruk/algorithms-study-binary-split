import java.io.*;
import java.util.*;

public class Attributes
{
    /**
     * This class is used for storing an attribute and its values of every person in the configuration file,
     * such as 'eyeColor red green blue black brown etc' so that the player classes can apply them for guessing and referencing.
     * @name stands for a specific attribute
     * @values represents values of the specified attribute which occur in the config file.
     * @config represents a configuration file that this class gathers the data of a specific attribute.
     */
    private String name;
    private ArrayList<String> values = new ArrayList<>();
    private File config;

    public Attributes(String name, String config) throws FileNotFoundException              //Specifying the name of an attribute so that the class will know which name and values
                                                                                            //it should gather.
    {
        this.name = name;

        this.config = new File(config);

        try(Scanner input = new Scanner(this.config))
        {
            String checkName = " ";

            while (checkName.length() != 0)
            /* read the file until the end of attributes' section (the group of attributes used for people
               in guessWho game before meeting the first person's name).
               e.g. eyeColor red green blue       <-- if we specify eyeColor as this class's name, it will store all of these values
                    hairColor red green blue
                    ..
                    ..
                                            <-- read until meet this line during this the class will populate data of a specified attribute
                    P1
                    eyeColor red
                    ..
                    .. */
            {
                checkName = input.nextLine();

                if(checkName.length() == 0)                              //if it reaches the end of the attributes section, stop and prepare to write the specified data
                    break;

                StringTokenizer st = new StringTokenizer(checkName);
                String attName = st.nextToken();
                if(attName.compareTo(name) == 0)                         //if the first token of a line is the same as the specified attribute name, then populate the values
                {
                    while(st.hasMoreElements())
                    {
                        values.add(st.nextToken());                     //e.g. eyeColor adding its values into ArryList<> values
                    }
                }
            }
        }

        Collections.sort(values);                                       //sorting this arrayList benefit for binary search
    }

    public String getName()
    {
        return name;
    }

    public String getRandomValue()                                     //e.g, does the person have blue eyes? etc.
    {
        Random rand = new Random();
        return values.get(rand.nextInt(values.size()));
    }

    public String getValue(String value)
    {
        for(String v: values) {
            if (v.compareTo(value) == 0)
                return v;
        }
        return null;
    }

    public void removeAsked(String askedValue)                          //invoked when some value is already asked
    {
        values.remove(askedValue);
    }

    /* Just for checking the correctness of data storing */
    public void printValues()
    {
        System.out.println("AttName: " + getName());
        for(String values: values)
            System.out.println("Value: " + values);
    }

    public ArrayList<String> getValues()
    {
        return values;
    }

}
