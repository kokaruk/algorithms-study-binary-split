import java.util.*;
import java.io.*;

public class Attribute
{
    private File config = new File("config.txt");
    private HashMap<String,String> atts = new HashMap<>();
    private String name;

    public Attribute(String name) throws IOException
    {
        this.name = name;

        try(Scanner input = new Scanner(config))
        {
            while (input.nextLine().compareTo(name) != 0)               //Find the specified person
            {
                input.nextLine();
            }

            input.nextLine();                                           //Skip the person's name

            while(input.hasNext())
            {
                String attName = input.next();                          //Read the name of attribute
                String value = input.next();                            //Read the value of attribute
                atts.put(attName,value);
            }                                                           //End of adding the person attributes
        }
    }

    public String getName()
    {
        return this.name;
    }

    public String getValue(String attName)
    {
        String value = null;

        return value;
    }
}
