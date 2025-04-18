package edu.guilford.ctisfinal.Backend.Map;

/***
 * State class to represent a state in the US map
 */
public class State {


    private int frequency;

    private String abbreviation;

    public State(String abbreviation, int frequency) {
        this.frequency = frequency;
        this.abbreviation = abbreviation;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return "State{" +
                "frequency=" + frequency +
                ", abbreviation='" + abbreviation + '\'' +
                '}';
    }

}
