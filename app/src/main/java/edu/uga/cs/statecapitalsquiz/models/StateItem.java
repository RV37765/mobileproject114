package edu.uga.cs.statecapitalsquiz.models;

/**
 * Represents a US state with its capital and two additional large cities.
 * Used for quiz questions where the capital is the correct answer and
 * the other cities serve as plausible wrong answers.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class StateItem {
    private int id;
    private String stateName;
    private String capitalCity;
    private String city2;
    private String city3;
    private int statehoodYear;
    private int capitalSinceYear;
    private int capitalRank;

    /**
     * Default constructor
     */
    public StateItem() {
    }

    /**
     * Full constructor with all fields
     */
    public StateItem(int id, String stateName, String capitalCity, String city2,
                     String city3, int statehoodYear, int capitalSinceYear, int capitalRank) {
        this.id = id;
        this.stateName = stateName;
        this.capitalCity = capitalCity;
        this.city2 = city2;
        this.city3 = city3;
        this.statehoodYear = statehoodYear;
        this.capitalSinceYear = capitalSinceYear;
        this.capitalRank = capitalRank;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public String getCity2() {
        return city2;
    }

    public void setCity2(String city2) {
        this.city2 = city2;
    }

    public String getCity3() {
        return city3;
    }

    public void setCity3(String city3) {
        this.city3 = city3;
    }

    public int getStatehoodYear() {
        return statehoodYear;
    }

    public void setStatehoodYear(int statehoodYear) {
        this.statehoodYear = statehoodYear;
    }

    public int getCapitalSinceYear() {
        return capitalSinceYear;
    }

    public void setCapitalSinceYear(int capitalSinceYear) {
        this.capitalSinceYear = capitalSinceYear;
    }

    public int getCapitalRank() {
        return capitalRank;
    }

    public void setCapitalRank(int capitalRank) {
        this.capitalRank = capitalRank;
    }

    @Override
    public String toString() {
        return "StateItem{" +
                "id=" + id +
                ", stateName='" + stateName + '\'' +
                ", capitalCity='" + capitalCity + '\'' +
                ", city2='" + city2 + '\'' +
                ", city3='" + city3 + '\'' +
                '}';
    }
}