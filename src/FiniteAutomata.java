import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zach on 3/8/2018.
 */
public class FiniteAutomata {
    private List<Integer> acceptStates;

    HashMap<Integer, TransitionContainer> machineRules = new HashMap<>();

    private boolean isValid = true;

    private Type machineType;

    FiniteAutomata(){
        machineType = Type.DFA;
    }

    public void setAcceptStates(String acceptStatesString)
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

    public void addRule(String ruleString) {
        List<String> splitString = Arrays.asList(ruleString.split(","));
        Integer currentState = Integer.parseInt(splitString.get(0));
        Integer transition = (int)(splitString.get(1).charAt(0));
        Integer nextState = Integer.parseInt(splitString.get(2));
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

        if (currentState < 0 || currentState > 255 || transition < 0 || transition > 255 || nextState < 0 || nextState > 255)
        {
            isValid = false;
        }
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

    public boolean isValid(){return isValid;}
}
