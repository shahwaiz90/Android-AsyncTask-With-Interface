
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AbstractWorkerClass {
    TextView someText;
    String TAG = "MainActivity";
    WorkerTaskFromAsync workerTaskFromAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "1 - onCreate");
        Log.i(TAG, "onCreate: Current Thread: "+Thread.currentThread().getId());
        someText = findViewById(R.id.someText);
    }

    //Initiates when user clicks on Start Button
    public void onStartButton(View view) {
        workerTaskFromAsync = new WorkerTaskFromAsync();
        workerTaskFromAsync.setListener(new WorkerTaskFromAsync.InnerWorkerInterface() {
            @Override
            public void onWorkStarted(boolean status) {
                Log.i(TAG, "Work is Started");
                updateUI("Work is started");
            }

            @Override
            public void onWorkFinished(boolean status) {
                Log.i(TAG, "Work is Finished, and Logging Out.");
                updateUI("Logging Out!");
                logoutScenario();
                workerTaskFromAsync.removeListener();
            }
        });
        workerTaskFromAsync.execute();
    }


    //Initiates when user clicks on Stop Button
    public void onStopButton(View view) {
        if(workerTaskFromAsync!=null){
            workerTaskFromAsync.cancel(true);
        }
    }

    private void updateUI(String str){
        Log.i(TAG, "Class: "+MainActivity.this);
        someText.setText(str);
        Toast.makeText(getApplicationContext(),"Toast!", Toast.LENGTH_SHORT).show();
    }

    private static class WorkerTaskFromAsync extends AsyncTask<Void, Void, Integer> {
        interface InnerWorkerInterface{
            void onWorkStarted(boolean status);
            void onWorkFinished(boolean status);
        }
        InnerWorkerInterface workerInterface;
        private void setListener(InnerWorkerInterface innerWorkerInterface){
            workerInterface = innerWorkerInterface;
        }

        void removeListener(){
            workerInterface = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            workerInterface.onWorkStarted(true);
            Log.i("MainActivity", " Thread from onPreExecute: "+Thread.currentThread().getId());
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.i("MainActivity", " Thread from doInBackground: "+Thread.currentThread().getId());
            for(int i=0; i < 10; i++){
                if(isCancelled()){
                    break;
                }
                try{
                    Log.i("MainActivity", "Id: "+i);
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer value) {
            Log.i("MainActivity", " Thread from onPostExecute: "+Thread.currentThread().getId());
            super.onPostExecute(value);
            if(workerInterface!=null){
                workerInterface.onWorkFinished(true);
            }else{
                Log.i("MainActivity","Worker Interface is null");
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "2 - On Start!!");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "3 - On Resume!!");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.i(TAG, "4 - On Pause!!");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "5 - On Stop!!");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "6 - On Destroy!!");
    }
}
