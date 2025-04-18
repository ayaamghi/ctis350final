package edu.guilford.ctisfinal.Backend.Map;

import edu.guilford.ctisfinal.Backend.CsvParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * USMap class to represent the US map and its states for the map visualization tool
 */
public class USMap {


    String[] stateNames = {
            "AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA",
            "HI","ID","IL","IN","IA","KS","KY","LA","ME","MD",
            "MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ",
            "NM","NY","NC","ND","OH","OK","OR","PA","RI","SC",
            "SD","TN","TX","UT","VT","VA","WA","WV","WI","WY",
            "DC"
    };


    private final ArrayList<State> states = new ArrayList<>();
    public USMap(CsvParser df) throws IOException {
        Map<String, Integer> stateMap = countStates(df);

        for (String state : stateNames) {
            states.add(new State(state, stateMap.getOrDefault(state, 0)));
        }



    }


    public ArrayList<State> getStates() {
        return states;
    }

    /***
     * Count the number of tweets per state-- pass in df to avoid circular calls in ContextManager
     * @param df
     * @return
     * @throws IOException
     */
    public  Map<String,Integer> countStates(CsvParser df) throws IOException {

        // 2) Grab the “state” column (all the two‑letter codes, or null/empty)
        List<String> codes = df.getColumn("state");

        // 3) Tally them up
        Map<String,Integer> freq = new HashMap<>();
        for (String code : codes) {
            if (code == null || code.isBlank()) {
                continue;
            }
            code = code.trim().toUpperCase();
            freq.merge(code, 1, Integer::sum);
        }
        return freq;
    }

}
