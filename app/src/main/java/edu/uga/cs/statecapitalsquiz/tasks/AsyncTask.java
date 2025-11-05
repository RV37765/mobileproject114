package edu.uga.cs.statecapitalsquiz.tasks;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
//
/**
 * A custom implementation of the deprecated AsyncTask to be used in modern Android versions.
 * This class allows background operations and publishing results on the UI thread.
 *
 * @param <Params>   the type of the parameters sent to the task upon execution.
 * @param <Progress> the type of the progress units published during the background computation.
 * @param <Result>   the type of the result of the background computation.
 */
public abstract class AsyncTask<Params, Progress, Result> {

    private static final String TAG = "AsyncTask";

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * This method is invoked on the background thread immediately after onPreExecute()
     * and before onPostExecute().
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     */
    protected abstract Result doInBackground(Params... params);

    /**
     * Runs on the UI thread before doInBackground().
     */
    protected void onPreExecute() {
        // Default is no-op
    }

    /**
     * Runs on the UI thread after doInBackground(). The specified result is the value
     * returned by doInBackground().
     *
     * @param result The result of the operation computed by doInBackground().
     */
    protected void onPostExecute(Result result) {
        // Default is no-op
    }

    /**
     * Executes the task with the given parameters. This method must be invoked on the UI thread.
     *
     * @param params The parameters of the task.
     */
    public final void execute(Params... params) {
        onPreExecute();
        executor.execute(() -> {
            Result result = doInBackground(params);
            handler.post(() -> onPostExecute(result));
        });
    }
}
