package hdm.csm.emergency;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportDetailActivity extends AppCompatActivity {
    Report report;

    TextView tv_name;
    TextView tv_birthday;
    TextView tv_commentsRoad;
    TextView tv_commentsVehicle;
    TextView tv_commentsWitness;


    ListView listView;
    LinearLayout ll_roadReportImage;
    ArrayAdapter<String> adapter;

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

        ArrayList<String> entries = new ArrayList<>();
//        entries.add(report.getRoadReportComment());
//        entries.add("Temperature: 10°c");
//        entries.add("Location: Lady Lane 23, PA12LH Paisley");



        listView = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entries);
        listView.setAdapter(adapter);

        //Set image views
        ll_roadReportImage = (LinearLayout) findViewById(R.id.imageContainer);

        ArrayList<String> paths = report.getRoadReportImageURIs();

        for (int i = 0; i < paths.size(); i++) {
            addImageToScrollView(Uri.parse(paths.get(i)), ll_roadReportImage);
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
