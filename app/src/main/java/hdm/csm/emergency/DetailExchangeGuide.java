package hdm.csm.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailExchangeGuide extends AppCompatActivity {

    ListView detailXchgTermsListView;
    String[] detailXchgTermsStringList;
    ArrayList<String> detailXchgTermsList;
    ListAdapter detailXchgTermsListAdap;

    ListView detailXchgActions1ListView;
    String[] detailXchgActions1StringList;
    ArrayList<String> detailXchgActions1List;
    ListAdapter detailXchgActions1ListAdap;

    ListView detailXchgActions2ListView;
    String[] detailXchgActions2StringList;
    ArrayList<String> detailXchgActions2List;
    ListAdapter detailXchgActions2ListAdap;

    Button detailXchgFileReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_exchange_guide);

        final Button detailXchgFileReportButton = (Button) findViewById(R.id.detail_xchg_file_report_btn);
        detailXchgFileReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(DetailExchangeGuide.this, CreateReportActivity.class));
            }
        });

        detailXchgTermsListView = (ListView) findViewById(R.id.detail_xchg_terms_lv);
        detailXchgTermsStringList = getResources().getStringArray(R.array.detail_xchg_guide_terms_arr);
        detailXchgTermsList = new ArrayList<String>();
        detailXchgTermsList.addAll(Arrays.asList(detailXchgTermsStringList));
        detailXchgTermsListAdap = new ArrayAdapter<String>(DetailExchangeGuide.this, R.layout.bullet_list_row, detailXchgTermsList);
        detailXchgTermsListView.setAdapter(detailXchgTermsListAdap);
        detailXchgTermsListView.setEnabled(false);
        detailXchgTermsListView.setScrollContainer(false);
        justifyListViewHeightBasedOnChildren(detailXchgTermsListView);

        detailXchgActions1ListView = (ListView) findViewById(R.id.detail_xchg_actions_1_lv);
        detailXchgActions1StringList = getResources().getStringArray(R.array.detail_xchg_guide_actions_1_arr);
        detailXchgActions1List = new ArrayList<String>();
        detailXchgActions1List.addAll(Arrays.asList(detailXchgActions1StringList));
        detailXchgActions1ListAdap = new ArrayAdapter<String>(DetailExchangeGuide.this, R.layout.bullet_list_row, detailXchgActions1List);
        detailXchgActions1ListView.setAdapter(detailXchgActions1ListAdap);
        detailXchgActions1ListView.setEnabled(false);
        detailXchgActions1ListView.setScrollContainer(false);
        justifyListViewHeightBasedOnChildren(detailXchgActions1ListView);

        detailXchgActions2ListView = (ListView) findViewById(R.id.detail_xchg_actions_2_lv);
        detailXchgActions2StringList = getResources().getStringArray(R.array.detail_xchg_guide_actions_2_arr);
        detailXchgActions2List = new ArrayList<String>();
        detailXchgActions2List.addAll(Arrays.asList(detailXchgActions2StringList));
        detailXchgActions2ListAdap = new ArrayAdapter<String>(DetailExchangeGuide.this, R.layout.bullet_list_row, detailXchgActions2List);
        detailXchgActions2ListView.setAdapter(detailXchgActions2ListAdap);
        detailXchgActions2ListView.setEnabled(false);
        detailXchgActions2ListView.setScrollContainer(false);
        justifyListViewHeightBasedOnChildren(detailXchgActions2ListView);
    }

    public void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + listItem.getPaddingTop() + listItem.getPaddingBottom();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
