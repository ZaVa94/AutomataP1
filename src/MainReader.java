import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Zach on 3/8/2018.
 */
public class MainReader {
    public static void main(String[] args)
    {
        initDirs();
        int numFiles = 0;
        File machineDir = Paths.get(args[0]).toFile();
        File inputFile = Paths.get(args[1]).toFile();
        int numberOfInputStrings = 0;
        for (File machineFile : machineDir.listFiles())
        {
            FiniteAutomata automata = buildMachine(machineFile);
            if (automata.isValid())
            {
                try {
                    Scanner scanner = new Scanner(inputFile);
                    while (scanner.hasNext())
                    {
                        automata.runString(scanner.next());
                        numberOfInputStrings += 1;
                    }
                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            File outputFile = new File("./Outputs/" + automata.getBaseName() + ".txt");
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                for (String acceptedString : automata.getAcceptedStrings())
                {
                    writer.write(acceptedString);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            File descriptionFile = new File("./Logs/" + automata.getBaseName() + ".log");
            automata.compileStates();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(descriptionFile));
                writer.write("Valid: " + automata.getMachineType());
                writer.write("States: " + automata.getNumberOfStates());
                writer.write("Accepted Strings: " + automata.getAcceptedStrings().size()+ "/" + numberOfInputStrings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initDirs() {
        if (Paths.get("./Logs").toFile() == null)
        {
            boolean logFileCreated = new File("./Logs").mkdir();
            System.out.println(logFileCreated);
        }
        if (Paths.get("./Outputs").toFile() == null)
        {
            boolean outputFileCreated = new File("./Outputs").mkdir();
            System.out.println(outputFileCreated);
        }
    }

    private static FiniteAutomata buildMachine(File machineFile) {
        FiniteAutomata finiteAutomata = new FiniteAutomata();
        finiteAutomata.setBaseName(machineFile.getName());
        int lineNumber = 0;

        try {
            Scanner scanner = new Scanner(machineFile);
            while (scanner.hasNext() && finiteAutomata.isValid())
            {
                if (lineNumber == 0)
                {
                    String stateString = scanner.next();
                    finiteAutomata.setAcceptStatesFromString(stateString);
                }
                else
                {
                    String ruleString = scanner.next();
                    finiteAutomata.addRule(ruleString);
                }
                lineNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't scan file.");
        }
        return null;
    }
}
