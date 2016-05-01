package hdm.csm.emergency;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

        ArrayList<String> paths = user.getPaths();


        listView = (ListView) findViewById(R.id.list);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PastReportsActivity.this);
                alertBuilder.setTitle("Delete this report?");

                alertBuilder.setMessage("Are you sure you want to delete this item?");
                final int positionToRemove = position;
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        user.removePath(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }
                });

                alertBuilder.show();
                return true;
            }
        });


        ArrayList<String> names = new ArrayList<>();

        //Create list
        for (int i = 0; i < reports.size(); i++) {
            Log.i("Reports", Integer.toString(reports.size()));
            names.add(reports.get(i).getRoadReportComment());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paths);
        listView.setAdapter(adapter);
    }
}
