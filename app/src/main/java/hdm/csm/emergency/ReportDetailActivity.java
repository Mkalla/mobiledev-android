package hdm.csm.emergency;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportDetailActivity extends AppCompatActivity {
    Report report;

    TextView tv_name;
    TextView tv_birthday;
    TextView tv_commentsRoad;
    TextView tv_commentsVehicle;
    TextView tv_commentsWitness;
    TextView tv_temperature;
    TextView tv_weatherType;

    LinearLayout ll_roadReportImage;
    LinearLayout ll_vehicleReportImage;
    LinearLayout ll_witnessReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        //Get Data and cast to report
        report = (Report) getIntent().getSerializableExtra("report");

        //Set textviews
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(report.getUser().getSurname() + ", " + report.getUser().getForename());

        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_birthday.setText(report.getUser().getBirthday());

        tv_commentsRoad = (TextView) findViewById(R.id.tv_commentsRoad);
        tv_commentsRoad.setText(report.getRoadReportComment());

        tv_commentsVehicle = (TextView) findViewById(R.id.tv_commentsVehicle);
        tv_commentsVehicle.setText(report.getVehicleReportComment());

        tv_commentsWitness = (TextView) findViewById(R.id.tv_commentsWitness);
        tv_commentsWitness.setText(report.getWitnessReportComment());

        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_temperature.setText(report.getTemperature());

        tv_weatherType = (TextView) findViewById(R.id.tv_weather);
        tv_weatherType.setText(report.getWeatherType());

        //Set image views
        ll_roadReportImage = (LinearLayout) findViewById(R.id.roadImageContainer);

        ArrayList<String> roadImgPaths = report.getRoadReportImageURIs();

        for (int i = 0; i < roadImgPaths.size(); i++) {
            addImageToScrollView(Uri.parse(roadImgPaths.get(i)), ll_roadReportImage);
        }

        //Set image views
        ll_vehicleReportImage = (LinearLayout) findViewById(R.id.vehicleImageContainer);

        ArrayList<String> vehicleImgPaths = report.getVehicleReportImageURIs();

        for (int i = 0; i < vehicleImgPaths.size(); i++) {
            addImageToScrollView(Uri.parse(vehicleImgPaths.get(i)), ll_vehicleReportImage);
        }

        //Set image views
        ll_witnessReportImage = (LinearLayout) findViewById(R.id.witnessImageContainer);

        ArrayList<String> witnessImgPaths = report.getWitnessReportImageURIs();

        for (int i = 0; i < witnessImgPaths.size(); i++) {
            addImageToScrollView(Uri.parse(witnessImgPaths.get(i)), ll_witnessReportImage);
        }
    }

    private void addImageToScrollView(Uri uri, LinearLayout view) {
        ImageView tempImageView = new ImageView(this);

        Bitmap imageBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 10;

        imageBitmap = BitmapFactory.decodeFile(uri.getPath(), options);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(15, 0, 15, 0);

        tempImageView.setLayoutParams(lp);
        tempImageView.setImageBitmap(imageBitmap);

        view.addView(tempImageView);
    }
}
