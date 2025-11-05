package edu.uga.cs.statecapitalsquiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.models.StateItem;

/**
 * Database operations class for CRUD operations on states and quizzes.
 * Provides methods to insert, retrieve, update, and delete data.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class QuizData {

    private static final String TAG = "QuizData";

    private SQLiteDatabase db;
    private QuizDBHelper dbHelper;
    private Context context;

    /**
     * Constructor
     *
     * @param context Application context
     */
    public QuizData(Context context) {
        this.context = context;
        this.dbHelper = new QuizDBHelper(context);
    }

    /**
     * Open database connection
     */
    public void open() {
        db = dbHelper.getWritableDatabase();
        Log.d(TAG, "Database opened");
    }

    /**
     * Close database connection
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        Log.d(TAG, "Database closed");
    }

    /**
     * Check if states table is empty
     *
     * @return true if no states in database
     */
    public boolean isDBEmpty() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + QuizDBHelper.TABLE_STATES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    // ==================== STATE OPERATIONS ====================

    /**
     * Insert a state into the database
     *
     * @param state StateItem to insert
     * @return Row ID of inserted state
     */
    public long insertState(StateItem state) {
        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.STATES_NAME, state.getStateName());
        values.put(QuizDBHelper.STATES_CAPITAL, state.getCapitalCity());
        values.put(QuizDBHelper.STATES_CITY2, state.getCity2());
        values.put(QuizDBHelper.STATES_CITY3, state.getCity3());
        values.put(QuizDBHelper.STATES_STATEHOOD_YEAR, state.getStatehoodYear());
        values.put(QuizDBHelper.STATES_CAPITAL_SINCE, state.getCapitalSinceYear());
        values.put(QuizDBHelper.STATES_CAPITAL_RANK, state.getCapitalRank());

        long result = db.insert(QuizDBHelper.TABLE_STATES, null, values);
        Log.d(TAG, "Inserted state: " + state.getStateName() + " (ID: " + result + ")");
        return result;
    }

    /**
     * Get all states from database
     *
     * @return List of all StateItem objects
     */
    public List<StateItem> getAllStates() {
        List<StateItem> states = new ArrayList<>();

        Cursor cursor = db.query(
                QuizDBHelper.TABLE_STATES,
                null, null, null, null, null,
                QuizDBHelper.STATES_NAME + " ASC"
        );

        while (cursor.moveToNext()) {
            StateItem state = new StateItem();
            state.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_ID)));
            state.setStateName(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_NAME)));
            state.setCapitalCity(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL)));
            state.setCity2(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CITY2)));
            state.setCity3(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CITY3)));
            state.setStatehoodYear(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_STATEHOOD_YEAR)));
            state.setCapitalSinceYear(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL_SINCE)));
            state.setCapitalRank(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL_RANK)));

            states.add(state);
        }

        cursor.close();
        Log.d(TAG, "Retrieved " + states.size() + " states");
        return states;
    }

    /**
     * Get a specific state by ID
     *
     * @param id State ID
     * @return StateItem or null if not found
     */
    public StateItem getStateById(int id) {
        Cursor cursor = db.query(
                QuizDBHelper.TABLE_STATES,
                null,
                QuizDBHelper.STATES_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        StateItem state = null;
        if (cursor.moveToFirst()) {
            state = new StateItem();
            state.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_ID)));
            state.setStateName(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_NAME)));
            state.setCapitalCity(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL)));
            state.setCity2(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CITY2)));
            state.setCity3(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CITY3)));
            state.setStatehoodYear(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_STATEHOOD_YEAR)));
            state.setCapitalSinceYear(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL_SINCE)));
            state.setCapitalRank(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.STATES_CAPITAL_RANK)));
        }

        cursor.close();
        return state;
    }

    /**
     * Select N random unique state IDs
     *
     * @param count Number of states to select (typically 6)
     * @return Array of random state IDs with no duplicates
     */
    public int[] selectRandomStates(int count) {
        List<StateItem> allStates = getAllStates();

        if (allStates.size() < count) {
            Log.e(TAG, "Not enough states in database!");
            return new int[0];
        }

        Random random = new Random();
        Set<Integer> selectedIds = new HashSet<>();

        // Keep selecting until we have enough unique IDs
        while (selectedIds.size() < count) {
            int randomIndex = random.nextInt(allStates.size());
            selectedIds.add(allStates.get(randomIndex).getId());
        }

        // Convert Set to int array
        int[] result = new int[count];
        int i = 0;
        for (Integer id : selectedIds) {
            result[i++] = id;
        }

        Log.d(TAG, "Selected " + count + " random state IDs");
        return result;
    }

    // ==================== QUIZ OPERATIONS ====================

    /**
     * Create a new quiz with given state IDs
     *
     * @param stateIds Array of 6 state IDs for the quiz
     * @return Quiz ID of newly created quiz
     */
    public long createNewQuiz(int[] stateIds) {
        if (stateIds.length != 6) {
            Log.e(TAG, "Quiz must have exactly 6 states!");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.QUIZZES_SCORE, 0);
        values.put(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED, 0);
        values.put(QuizDBHelper.QUIZZES_STATE1, stateIds[0]);
        values.put(QuizDBHelper.QUIZZES_STATE2, stateIds[1]);
        values.put(QuizDBHelper.QUIZZES_STATE3, stateIds[2]);
        values.put(QuizDBHelper.QUIZZES_STATE4, stateIds[3]);
        values.put(QuizDBHelper.QUIZZES_STATE5, stateIds[4]);
        values.put(QuizDBHelper.QUIZZES_STATE6, stateIds[5]);

        long quizId = db.insert(QuizDBHelper.TABLE_QUIZZES, null, values);
        Log.d(TAG, "Created new quiz with ID: " + quizId);
        return quizId;
    }

    /**
     * Update quiz progress (score and questions answered)
     *
     * @param quizId Quiz ID to update
     * @param score Current score
     * @param questionsAnswered Number of questions answered so far
     */
    public void updateQuizProgress(int quizId, int score, int questionsAnswered) {
        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.QUIZZES_SCORE, score);
        values.put(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED, questionsAnswered);

        int rows = db.update(
                QuizDBHelper.TABLE_QUIZZES,
                values,
                QuizDBHelper.QUIZZES_ID + "=?",
                new String[]{String.valueOf(quizId)}
        );

        Log.d(TAG, "Updated quiz " + quizId + " - Score: " + score + ", Answered: " + questionsAnswered);
    }

    /**
     * Mark quiz as complete with final score and date
     *
     * @param quizId Quiz ID to complete
     * @param finalScore Final score
     * @param date Completion date/time
     */
    public void completeQuiz(int quizId, int finalScore, String date) {
        ContentValues values = new ContentValues();
        values.put(QuizDBHelper.QUIZZES_SCORE, finalScore);
        values.put(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED, 6);
        values.put(QuizDBHelper.QUIZZES_DATE, date);

        db.update(
                QuizDBHelper.TABLE_QUIZZES,
                values,
                QuizDBHelper.QUIZZES_ID + "=?",
                new String[]{String.valueOf(quizId)}
        );

        Log.d(TAG, "Completed quiz " + quizId + " with score: " + finalScore);
    }

    /**
     * Get a specific quiz by ID
     *
     * @param quizId Quiz ID
     * @return Quiz object or null if not found
     */
    public Quiz getQuizById(int quizId) {
        Cursor cursor = db.query(
                QuizDBHelper.TABLE_QUIZZES,
                null,
                QuizDBHelper.QUIZZES_ID + "=?",
                new String[]{String.valueOf(quizId)},
                null, null, null
        );

        Quiz quiz = null;
        if (cursor.moveToFirst()) {
            quiz = new Quiz();
            quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_ID)));
            quiz.setDate(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_DATE)));
            quiz.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_SCORE)));
            quiz.setQuestionsAnswered(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED)));

            int[] stateIds = new int[6];
            stateIds[0] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE1));
            stateIds[1] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE2));
            stateIds[2] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE3));
            stateIds[3] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE4));
            stateIds[4] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE5));
            stateIds[5] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE6));
            quiz.setStateIds(stateIds);
        }

        cursor.close();
        return quiz;
    }


    /**
     * Get all completed quizzes ordered by date (newest first)
     *
     * @return List of completed Quiz objects
     */
    public List<Quiz> getAllCompletedQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        Cursor cursor = db.query(
                QuizDBHelper.TABLE_QUIZZES,
                null,
                QuizDBHelper.QUIZZES_DATE + " IS NOT NULL",
                null, null, null,
                QuizDBHelper.QUIZZES_DATE + " DESC"
        );

        while (cursor.moveToNext()) {
            Quiz quiz = new Quiz();
            quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_ID)));
            quiz.setDate(cursor.getString(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_DATE)));
            quiz.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_SCORE)));
            quiz.setQuestionsAnswered(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED)));

            int[] stateIds = new int[6];
            stateIds[0] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE1));
            stateIds[1] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE2));
            stateIds[2] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE3));
            stateIds[3] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE4));
            stateIds[4] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE5));
            stateIds[5] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE6));
            quiz.setStateIds(stateIds);

            quizzes.add(quiz);
        }

        cursor.close();
        Log.d(TAG, "Retrieved " + quizzes.size() + " completed quizzes");
        return quizzes;
    }

    /**
     * Get the most recent incomplete quiz
     *
     * @return Quiz object or null if no incomplete quiz exists
     */
    public Quiz getCurrentQuiz() {
        Cursor cursor = db.query(
                QuizDBHelper.TABLE_QUIZZES,
                null,
                QuizDBHelper.QUIZZES_DATE + " IS NULL",
                null, null, null,
                QuizDBHelper.QUIZZES_ID + " DESC",
                "1"
        );

        Quiz quiz = null;
        if (cursor.moveToFirst()) {
            quiz = new Quiz();
            quiz.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_ID)));
            quiz.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_SCORE)));
            quiz.setQuestionsAnswered(cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_QUESTIONS_ANSWERED)));

            int[] stateIds = new int[6];
            stateIds[0] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE1));
            stateIds[1] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE2));
            stateIds[2] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE3));
            stateIds[3] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE4));
            stateIds[4] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE5));
            stateIds[5] = cursor.getInt(cursor.getColumnIndexOrThrow(QuizDBHelper.QUIZZES_STATE6));
            quiz.setStateIds(stateIds);
        }

        cursor.close();
        return quiz;
    }
}