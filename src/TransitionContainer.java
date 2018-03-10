import java.util.HashMap;
import java.util.List;

/**
 * Created by Zach on 3/8/2018.
 */
public class TransitionContainer extends HashMap<Integer, List<Integer>> {
    public boolean keyContainsMultipleStates(Integer key)
    {
        return get(key).size() > 1;
    }
}
