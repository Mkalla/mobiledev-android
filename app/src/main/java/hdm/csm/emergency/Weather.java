package hdm.csm.emergency;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

public class Weather extends AppCompatActivity {
    /* Vars */
    TextView textView;
    TextView forecastJSON;
    String siteList;
    String baseUrl;
    String qry;
    String key;
    String reqUrl;
    String forecastReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final TextView textView = (TextView) findViewById(R.id.JSON);

        siteList = getSiteListJSON();
        textView.setText(siteList);
        getWeather();
    }

    public String getSiteListJSON(){
        String json = null;
        try{
            InputStream is = getAssets().open("sitelist.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch(IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void getWeather(){
        final TextView forecastJSON = (TextView) findViewById(R.id.forecastJSON);

        // Instantiate the RequestQueue.
        RequestQueue weatherQueue = Volley.newRequestQueue(this);

        baseUrl = "http://datapoint.metoffice.gov.uk/public/data/val/wxobs/all/json/";
        qry = "3134?res=hourly";
        key = "&key=3dd3210c-9aff-4547-9c28-9b590cc7d2c9";
        reqUrl = baseUrl + qry + key;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, reqUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        forecastJSON.setText("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                forecastJSON.setText("That didn't work!");
            }
        });

        weatherQueue.add(stringRequest);
    }
}
