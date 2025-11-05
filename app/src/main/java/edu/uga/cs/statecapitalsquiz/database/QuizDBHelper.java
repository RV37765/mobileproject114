package edu.uga.cs.statecapitalsquiz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for managing SQLite database creation and upgrades.
 * Creates two tables: states and quizzes.
 *
 * @author StateCapitalsQuiz Team
 * @version 1.0
 */
public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "state_quiz.db";
    private static final int DATABASE_VERSION = 1;

    // States table
    public static final String TABLE_STATES = "states";
    public static final String STATES_ID = "id";
    public static final String STATES_NAME = "state_name";
    public static final String STATES_CAPITAL = "capital_city";
    public static final String STATES_CITY2 = "city2";
    public static final String STATES_CITY3 = "city3";
    public static final String STATES_STATEHOOD_YEAR = "statehood_year";
    public static final String STATES_CAPITAL_SINCE = "capital_since_year";
    public static final String STATES_CAPITAL_RANK = "capital_rank";

    // Quizzes table
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_ID = "id";
    public static final String QUIZZES_DATE = "date";
    public static final String QUIZZES_SCORE = "score";
    public static final String QUIZZES_QUESTIONS_ANSWERED = "questions_answered";
    public static final String QUIZZES_STATE1 = "state1_id";
    public static final String QUIZZES_STATE2 = "state2_id";
    public static final String QUIZZES_STATE3 = "state3_id";
    public static final String QUIZZES_STATE4 = "state4_id";
    public static final String QUIZZES_STATE5 = "state5_id";
    public static final String QUIZZES_STATE6 = "state6_id";

    // Create states table SQL
    private static final String CREATE_STATES_TABLE =
            "CREATE TABLE " + TABLE_STATES + " (" +
                    STATES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STATES_NAME + " TEXT NOT NULL, " +
                    STATES_CAPITAL + " TEXT NOT NULL, " +
                    STATES_CITY2 + " TEXT NOT NULL, " +
                    STATES_CITY3 + " TEXT NOT NULL, " +
                    STATES_STATEHOOD_YEAR + " INTEGER, " +
                    STATES_CAPITAL_SINCE + " INTEGER, " +
                    STATES_CAPITAL_RANK + " INTEGER" +
                    ")";

    // Create quizzes table SQL
    private static final String CREATE_QUIZZES_TABLE =
            "CREATE TABLE " + TABLE_QUIZZES + " (" +
                    QUIZZES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUIZZES_DATE + " TEXT, " +
                    QUIZZES_SCORE + " INTEGER DEFAULT 0, " +
                    QUIZZES_QUESTIONS_ANSWERED + " INTEGER DEFAULT 0, " +
                    QUIZZES_STATE1 + " INTEGER, " +
                    QUIZZES_STATE2 + " INTEGER, " +
                    QUIZZES_STATE3 + " INTEGER, " +
                    QUIZZES_STATE4 + " INTEGER, " +
                    QUIZZES_STATE5 + " INTEGER, " +
                    QUIZZES_STATE6 + " INTEGER, " +
                    "FOREIGN KEY(" + QUIZZES_STATE1 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + "), " +
                    "FOREIGN KEY(" + QUIZZES_STATE2 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + "), " +
                    "FOREIGN KEY(" + QUIZZES_STATE3 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + "), " +
                    "FOREIGN KEY(" + QUIZZES_STATE4 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + "), " +
                    "FOREIGN KEY(" + QUIZZES_STATE5 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + "), " +
                    "FOREIGN KEY(" + QUIZZES_STATE6 + ") REFERENCES " + TABLE_STATES + "(" + STATES_ID + ")" +
                    ")";

    /**
     * Constructor
     *
     * @param context Application context
     */
    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATES_TABLE);
        db.execSQL(CREATE_QUIZZES_TABLE);
    }

    /**
     * Called when database needs to be upgraded
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATES);
        onCreate(db);
    }
}