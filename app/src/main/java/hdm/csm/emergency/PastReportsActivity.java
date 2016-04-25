package hdm.csm.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PastReportsActivity extends AppCompatActivity {


    User user;
    DataManager dataManager;
    ArrayList<Report> reports = new ArrayList<>();

    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_reports);

        user = User.getInstance(getApplicationContext());
        dataManager = DataManager.getInstance(getApplicationContext());

        listView = (ListView) findViewById (R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(PastReportsActivity.this, ReportDetailActivity.class);
                Report selectedReport = (Report) dataManager.readObjectFromMemory(user.getPaths().get(position));

                intent.putExtra("report", selectedReport);
                startActivity(intent);
            }
        });


        //Get all report paths from user shared pref
        ArrayList<String> paths = user.getPaths();

        //Get all reports and append to reports array list
//        for(int i = 0; i < paths.size(); i++){
//
//            Report report = (Report) dataManager.readObjectFromMemory(paths.get(i));
////            Log.i("Paths", );
//
//            reports.add(report);
//        }

        ArrayList<String> names = new ArrayList<>();

        //Create list
        for(int i = 0; i < reports.size(); i++){
            Log.i("Reports", Integer.toString(reports.size()));
            names.add(reports.get(i).getRoadReportComment());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paths);
        listView.setAdapter(adapter);
    }
}
