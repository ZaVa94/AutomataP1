import java.util.*;

/**
 * Created by Zach on 3/8/2018.
 */
public class FiniteAutomata {
    private List<Integer> acceptStates;

    private String baseName = "";

    private HashMap<Integer, TransitionContainer> machineRules = new HashMap<>();

    private boolean isValid = true;

    private Type machineType = Type.DFA;

    private int numberOfStates = 0;

    private int startState = 0;

    private List<String> acceptedStrings = new ArrayList<>();

    FiniteAutomata(){}

    void setAcceptStatesFromString(String acceptStatesString)
    {
        if (acceptStates == null)
        {
            acceptStates = new ArrayList<>();
        }
        for (String state : acceptStatesString.split(","))
        {
            acceptStates.add(Integer.parseInt(state));
        }
        isValid = checkStatesValidity();
    }

    public Type getMachineType() {
        return machineType;
    }

    void addRule(String ruleString) {
        boolean ruleIsValid = true;
        List<String> splitString = Arrays.asList(ruleString.split(","));
        Integer currentState = Integer.parseInt(splitString.get(0));
        Integer transition = (int)(splitString.get(1).charAt(0));
        Integer nextState = Integer.parseInt(splitString.get(2));

        if (currentState < 0 || currentState > 255 || transition < 0 || transition > 255 || nextState < 0 || nextState > 255)
        {
            isValid = false;
            ruleIsValid = false;
        }

        if (ruleIsValid)
        {
            TransitionContainer retrievedRuleSet = machineRules.get(currentState);
            if (retrievedRuleSet == null)
            {
                retrievedRuleSet = new TransitionContainer();
            }
            List<Integer> retrievedTransitionStates = retrievedRuleSet.get(transition);
            if (retrievedTransitionStates == null)
            {
                retrievedTransitionStates = new ArrayList<>();
            }
            retrievedTransitionStates.add(nextState);
            if (retrievedTransitionStates.size() > 1 || transition == 96)
            {
                machineType = Type.NFA;
            }
            retrievedRuleSet.put(transition, retrievedTransitionStates);
            machineRules.put(currentState, retrievedRuleSet);
        }
    }

    void setBaseName(String name) {
        baseName = name.substring(0, name.indexOf('.'));
    }

    String getBaseName() {
        return baseName;
    }

    void runString(String inputString)
    {
        String inputStringWOEOL;
        if (inputString.contains("\r\n"))
        {
            inputStringWOEOL = inputString.replace("\r\n", "");
        }
        else
        {
            inputStringWOEOL = inputString.replace("\n", "");
        }
        if (isValidString(inputStringWOEOL))
        {
            acceptedStrings.add(inputString);
        }
    }

    private boolean isValidString(String inputStringWOEOL) {
        int currentCharIndex = 0;
        Integer currentState = startState;
        while (currentCharIndex < inputStringWOEOL.length())
        {
            if (currentState == 255)
            {
                return false;
            }
            Integer transition = (int)inputStringWOEOL.charAt(currentCharIndex);
            TransitionContainer currentTransitionRules = machineRules.get(currentState);
            if (currentTransitionRules.get(transition) == null)
            {
                currentState = 255;
            }
            else if (currentTransitionRules.get(transition).size() > 1)
            {
                for (Integer newState : currentTransitionRules.get(transition))
                {
                    FiniteAutomata clone = this.copy();
                    clone.setStartState(newState);
                    clone.runString(inputStringWOEOL.substring(currentCharIndex + 1));
                    if (clone.getAcceptedStrings().size() > 0)
                    {
                        return true;
                    }
                }
                return false;
            }
            else if (transition.equals(96))
            {
                FiniteAutomata clone = this.copy();
                clone.setStartState(currentTransitionRules.get(transition).get(0));
                clone.runString(inputStringWOEOL.substring(currentCharIndex +1));
                if (clone.getAcceptedStrings().size() > 0)
                {
                    return true;
                }
            }
            else
            {
                currentState = currentTransitionRules.get(transition).get(0);
            }
            currentCharIndex += 1;
        }
        return acceptStates.contains(currentState);
    }

    FiniteAutomata copy()
    {
        FiniteAutomata copy = new FiniteAutomata();
        copy.setAcceptStates(this.acceptStates);
        copy.setMachineRules(this.machineRules);
        return copy;
    }

    private void setMachineRules(HashMap<Integer, TransitionContainer> machineRules) {
        this.machineRules = machineRules;
    }

    private void setAcceptStates(List<Integer> states)
    {
        this.acceptStates = states;
    }

    private enum Type {
        NFA, DFA, INVALID
    }

    private boolean checkStatesValidity()
    {
        if (acceptStates.size() > 0)
        {
            for (Integer state : acceptStates)
            {
                if (0 > state || state > 255)

                {
                    return false;
                }
            }
        }
        return true;
    }

    List<String> getAcceptedStrings()
    {
        return this.acceptedStrings;
    }

    private void setStartState(int state)
    {
        startState = state;
    }

    void compileStates()
    {
        for (Object o : machineRules.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getValue() != null) {
                numberOfStates += 1;
            }
        }
    }

    String getAlphabet()
    {
        List<Integer> alphabetList = new ArrayList<>();
        for (Object o : machineRules.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getValue() != null) {
                TransitionContainer value = (TransitionContainer) pair.getValue();
                Set<Integer> keys = value.keySet();
                alphabetList.add(value.keySet())
            }
        }
    }

    private void addItemsToList(List<Integer> alphabetList, Set<Integer> keys)
    {
        for (Integer key : keys)
        {

        }
    }

    int getNumberOfStates()
    {
        return this.numberOfStates;
    }

    boolean isValid(){return isValid;}
}
