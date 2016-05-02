package hdm.csm.emergency;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class EmergencyReactionGuide extends AppCompatActivity {

    ListView emGuideMotorwayListView;
    String[] emGuideMotorwayStringList;
    ArrayList<String> emGuideMotorwayList;
    ListAdapter emGuideMotorwayListAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_reaction_guide);

        emGuideMotorwayListView = (ListView) findViewById(R.id.emguide_motorway_list_view);
        emGuideMotorwayStringList = getResources().getStringArray(R.array.emergency_react_guide_motorway_arr);
        emGuideMotorwayList = new ArrayList<String>();
        emGuideMotorwayList.addAll(Arrays.asList(emGuideMotorwayStringList));

        emGuideMotorwayListAdap = new ArrayAdapter<String>(EmergencyReactionGuide.this, R.layout.bullet_list_row, emGuideMotorwayList);
        emGuideMotorwayListView.setAdapter(emGuideMotorwayListAdap);
    }
}
