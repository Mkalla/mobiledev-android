package hdm.csm.emergency;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CreateReportActivity
        extends AppCompatActivity
        implements Step1Fragment.OnFragmentInteractionListener,
        Step2Fragment.OnFragmentInteractionListener,
        Step3Fragment.OnFragmentInteractionListener,
        View.OnClickListener {

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

    Step1Fragment step1Fragment;
    Step2Fragment step2Fragment;
    Step3Fragment step3Fragment;

    ArrayList<String> roadReportImageURIs = new ArrayList<String>();
    ArrayList<String> vehicleReportImageURIs = new ArrayList<String>();
    ArrayList<String> witnessReportImageURIs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        //create Fragments
        step1Fragment = Step1Fragment.newInstance();
        step2Fragment = Step2Fragment.newInstance();
        step3Fragment = Step3Fragment.newInstance();

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
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void onFragmentInteraction(Uri uri) {
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
            switch (position) {
                case 0:
                    return step1Fragment;
                case 1:
                    return step2Fragment;
                case 2:
                    return step3Fragment;
                default:
                    return step1Fragment;
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

        report.setWeatherType(dataManager.getWeatherType());
        report.setTemperature(dataManager.getTemperature());


        report.setUser(User.getInstance(getApplicationContext()));

        //Createfile and save
        String filename = "report_" + System.currentTimeMillis();
        dataManager.saveReport(filename, report);
    }
}
