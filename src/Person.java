import java.util.*;
import java.io.*;

public class Person
{
    private File config;
    private HashMap<String,String> atts = new HashMap<>();
    private String name;

    public Person(String name, String config) throws IOException
    {
        this.name = name;

        this.config = new File(config);

        try(Scanner input = new Scanner(this.config))
        {
            String line = " ";
            int lineNumb = 0;

            while (line.length() != 0)                                  //Skip attribute lines (The lines before meeting person's name)
            {                                                           //And count the number of attributes per person
                line = input.nextLine();

                if(line.length() != 0)                                  //Found the end of attribute lines so the next line will be the person's name
                    lineNumb++;
            }

            boolean foundName = false;

            while (!foundName)                                          //Find the specified person
            {
                if(input.next().compareTo(name) == 0) {
                    foundName = true;
                }
            }

           for(int i = 0; i < lineNumb; i++)                            //Adding the person attributes
           {
                String attName = input.next();                          //Read the name of attribute
                String value = input.next();                            //Read the value of attribute
                atts.put(attName,value);
            }
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
            System.out.println("Key: " + mentry.getKey() + " \nVal: " + mentry.getValue());
        }
    }


    public void removeAttribute(String key)
    {
        atts.remove(key);
    }
}
