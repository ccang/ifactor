package littleangel.ifactor;


import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Button startButton;
    EditText inputText;
    TextView resultText;
    TextView resultText2;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start);
        inputText = (EditText) findViewById(R.id.inputNumber);
        resultText = (TextView) findViewById(R.id.result);
        resultText2 = (TextView) findViewById(R.id.result2);
        timeText = (TextView) findViewById(R.id.runtime);

        // Setup the event handlers for the Buttons in the main screen
        // if clicked, do factorization
        startButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                startButton.setEnabled(false);

                String inputStr = inputText.getText().toString();
                long inputNumber=0;
                boolean validInput=true;

                    try {
                        inputNumber = Long.valueOf(inputStr);
                    } catch (Exception e) {
                        resultText.setText("Input must be an integer!");
                        resultText2.setText("");
                        timeText.setText("");
                        validInput=false;
                    }

                    if (validInput) {
                        if (inputNumber >= 2) {

                            resultText.setText("Factorizing...");
                            resultText2.setText("");
                            timeText.setText("");
                            FactorTask runFactor = new FactorTask();
                            runFactor.execute(inputNumber);

                        } else {
                            resultText.setText("Input number must be an greater than 2!");
                            resultText2.setText("");
                            timeText.setText("");
                        }
                    }
                startButton.setEnabled(true);

            }
        });

    }

    public class FactorTask extends AsyncTask<Long, Void, Factors> {
        Factors num1;
        /** The system calls this to perform work in a worker thread and delivers it the parameters given to AsyncTask.execute() */
        protected Factors doInBackground(Long... number) {
            num1 = new Factors(number[0]);
            return num1;
        }
/*
        protected void onProgressUpdate(Integer... progress){
            //Update a progress bar here, or ignore it, it's up to you
            resultText.setText(progress[0] + "% completed...");
        }*/

        /* The system calls this to perform work in the UI thread and delivers the result from doInBackground() */
        protected void onPostExecute(Factors num1) {
            resultText.setText(Html.fromHtml(num1.getFormatNotation(false)));
            resultText2.setText("The " + num1.getTotalFactor() +" factors are: " + num1.getFormatDistinctFactor(", ") + "\n");
            timeText.setText("Factorization time = " + String.format("%.3f", num1.getRunningTime()) + " + " + String.format("%.3f", num1.getBuildingTime()) + " sec.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
