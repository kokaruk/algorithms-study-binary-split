import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class test {
    public static void main(String[] args) throws IOException {
        RandomGuessPlayer rd = new RandomGuessPlayer("config.txt","P3");
        rd.print();
    }
}
