import java.util.*;
import java.io.*;

public class Attribute
{
    File config = new File("config.txt");
    protected HashMap<String,String> atts = new HashMap<>();
    protected String name;

    public Attribute(String name) throws IOException
    {
        this.name = name;

        try(Scanner input = new Scanner(config))
        {
            boolean readComplete = false;

            while(!readComplete)
            {
                while (input.hasNextLine())
                {
                    if(input.nextLine().compareTo(name) == 0)
                    {

                    }
                }
            }
        }
    }
}
