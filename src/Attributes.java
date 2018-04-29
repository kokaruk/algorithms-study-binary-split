import java.io.*;
import java.util.*;

public class Attributes {
    private String name;
    private ArrayList<String> values = new ArrayList<>();
    private File config;

    public Attributes(String name, String config) throws FileNotFoundException {                      //specify the name of attribute for each line in the attribute section
        this.name = name;

        this.config = new File(config);

        try(Scanner input = new Scanner(this.config))
        {
            String checkName = " ";

            while (checkName.length() != 0)                                         //read until the end of attribute section before meeting the person name
            {
                checkName = input.nextLine();

                if(checkName.length() == 0)
                    break;

                StringTokenizer st = new StringTokenizer(checkName);
                String attName = st.nextToken();
                if(attName.compareTo(name) == 0)                                    //if the first token of each line is the same as the specified attribute name, then populate the values
                {
                    while(st.hasMoreElements())
                    {
                        values.add(st.nextToken());
                    }
                }
            }
        }

        Collections.sort(values);                                              //benefit for binary search
    }

    public String getName()
    {
        return name;
    }

    public String getRandomValue()                                                  //e.g, does the person have blue eyes?
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

    public void removeAsked(String askedValue)
    {
        values.remove(askedValue);
    }

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
