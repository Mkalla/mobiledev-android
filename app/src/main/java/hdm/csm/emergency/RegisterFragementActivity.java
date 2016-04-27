package hdm.csm.emergency;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisterFragementActivity
        extends AppCompatActivity
        implements Step1Fragment.OnFragmentInteractionListener,
        Step2Fragment.OnFragmentInteractionListener,
        Step3Fragment.OnFragmentInteractionListener,
        View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    Report report;
    DataManager dataManager;

    Button createReportButton;
    Button cancelButton;

    Uri mImageURI;

    Step1Fragment step1Fragment;
    Step2Fragment step2Fragment;
    Step3Fragment step3Fragment;

    ArrayList<String> roadReportImageURIs = new ArrayList<String>();
    ArrayList<String> vehicleReportImageURIs = new ArrayList<String>();
    ArrayList<String> witnessReportImageURIs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fragement);

        dataManager = DataManager.getInstance(getApplicationContext());

        createReportButton = (Button) findViewById(R.id.button_createReport);
        createReportButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_fragement, menu);
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

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position){
                case 0:
                    step1Fragment = Step1Fragment.newInstance();
                    return step1Fragment;
                case 1:
                    step2Fragment = Step2Fragment.newInstance();
                    return step2Fragment;
                case 2:
                    step3Fragment = Step3Fragment.newInstance();
                    return step3Fragment;
                default:
                    return Step1Fragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Road Conditions";
                case 1:
                    return "Vehicle Report";
                case 2:
                    return "Witness Report";
            }
            return null;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_cancel:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.button_createReport:
                Toast.makeText(this, "Report created", Toast.LENGTH_SHORT).show();
                createReport();
                setResult(Activity.RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void createReport() {

        report = new Report();

        //Get all road report fields
        report.setRoadReportImageURIs(this.roadReportImageURIs);
        report.setRoadReportComment(step1Fragment.etRoadReportComment.getText().toString());

        report.setVehicleReportImageURIs(this.vehicleReportImageURIs);
        report.setVehicleReportComment(step2Fragment.etVehicleReportComment.getText().toString());

        report.setWitnessReportImageURIs(this.witnessReportImageURIs);
        report.setWitnessReportComment(step3Fragment.etWitnessReportComment.getText().toString());

        report.setUser(User.getInstance(getApplicationContext()));

        //Createfile and save
        String filename = "report_" + System.currentTimeMillis();
        dataManager.saveReport(filename, report);
    }
}