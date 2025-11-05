package edu.uga.cs.statecapitalsquiz.utils;

import android.content.Context;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.statecapitalsquiz.models.StateItem;

/**
 * Utility class for parsing state_capitals.csv file from assets folder.
 * Reads CSV data and converts it to StateItem objects.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class CSVParser {

    private static final String TAG = "CSVParser";
    private Context context;

    /**
     * Constructor
     *
     * @param context Application context for accessing assets
     */
    public CSVParser(Context context) {
        this.context = context;
    }

    /**
     * Parse the state_capitals.csv file and return list of StateItem objects
     *
     * @return List of all 50 states with their data
     * @throws Exception if file cannot be read or parsed
     */
    public List<StateItem> parseCSV() throws Exception {
        List<StateItem> states = new ArrayList<>();

        try {
            // Open CSV file from assets folder
            InputStream inputStream = context.getAssets().open("state_capitals.csv");
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

            String[] nextLine;
            boolean firstLine = true;

            // Read each line
            while ((nextLine = reader.readNext()) != null) {
                // Skip header row
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Create StateItem from CSV data
                StateItem state = new StateItem();
                state.setStateName(nextLine[0]);
                state.setCapitalCity(nextLine[1]);
                state.setCity2(nextLine[2]);
                state.setCity3(nextLine[3]);

                // Parse optional year fields
                if (nextLine.length > 4 && !nextLine[4].isEmpty()) {
                    state.setStatehoodYear(Integer.parseInt(nextLine[4]));
                }
                if (nextLine.length > 5 && !nextLine[5].isEmpty()) {
                    state.setCapitalSinceYear(Integer.parseInt(nextLine[5]));
                }
                if (nextLine.length > 6 && !nextLine[6].isEmpty()) {
                    state.setCapitalRank(Integer.parseInt(nextLine[6]));
                }

                states.add(state);
                Log.d(TAG, "Parsed: " + state.getStateName());
            }

            reader.close();
            Log.d(TAG, "Total states parsed: " + states.size());

        } catch (Exception e) {
            Log.e(TAG, "Error parsing CSV", e);
            throw e;
        }

        return states;
    }
}