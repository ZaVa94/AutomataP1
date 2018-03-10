import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Zach on 3/8/2018.
 */
public class MainReader {
    public static void main(String[] args)
    {
        int numFiles = 0;
        File machineDir = Paths.get(args[0]).toFile();
        for (File machineFile : machineDir.listFiles())
        {
            FiniteAutomata automata = buildMachine(machineFile);
        }
    }

    private static FiniteAutomata buildMachine(File machineFile) {
        FiniteAutomata finiteAutomata = new FiniteAutomata();
        int lineNumber = 0;

        try {
            Scanner scanner = new Scanner(machineFile);
            while (scanner.hasNext() && finiteAutomata.isValid())
            {
                if (lineNumber == 0)
                {
                    String stateString = scanner.next();
                    finiteAutomata.setAcceptStates(stateString);
                    lineNumber++;
                }
                else
                {
                    String ruleString = scanner.next();
                    finiteAutomata.addRule(ruleString);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't scan file.");
        }
        return null;
    }
}
