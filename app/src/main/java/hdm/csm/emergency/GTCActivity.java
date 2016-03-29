package hdm.csm.emergency;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class GTCActivity extends AppCompatActivity {

    FileInputStream inputStream;
    String filename = "FAQ_File";
    FileOutputStream outputStream;

    TextView gtcTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gtc);

        gtcTextView = (TextView)findViewById(R.id.gtcTextView);

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write("This text comes from internal storage".getBytes());//getBytes converts string to byte array
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Read from internal storage
        try {
            inputStream = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            inputStream.close();
            // puts contents of file into a TextView
            gtcTextView.setText(out.toString());

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
