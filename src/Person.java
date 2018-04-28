import java.util.*;
import java.io.*;

public class Person
{
    private File config = new File("config.txt");
    private HashMap<String,String> atts = new HashMap<>();
    private String name;

    public Person(String name) throws IOException
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

    public String getPersonName()
    {
        return this.name;
    }

    public String getAttValue(String attName)
    {
        String value = atts.get(attName);
        return value;
    }

    public void printAttributes()
    {
        Set set = atts.entrySet();
        for (Object aSet : set) {
            Map.Entry mentry = (Map.Entry) aSet;
            System.out.println("key is: " + mentry.getKey() + " & Value is: " + mentry.getValue());
        }
    }

    public void removeElement(String key)
    {
        atts.remove(key);
    }
}
