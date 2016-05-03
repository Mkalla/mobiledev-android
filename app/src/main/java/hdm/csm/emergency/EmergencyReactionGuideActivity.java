package hdm.csm.emergency;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class EmergencyReactionGuideActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Button nextButton;
    private Button prevButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_reaction_guide);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int curr = mViewPager.getCurrentItem();

                if (curr == 2) {
                    nextButton.setEnabled(false);
                } else if (curr == 0) {
                    prevButton.setEnabled(false);
                } else {
                    nextButton.setEnabled(true);
                    prevButton.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextButton = (Button) findViewById(R.id.button_next);
        prevButton = (Button) findViewById(R.id.button_previous);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
    }

    public void next() {
        int curr = mViewPager.getCurrentItem();
        curr++;
        mViewPager.setCurrentItem(curr);
    }


    public void previous() {
        int curr = mViewPager.getCurrentItem();
        curr--;
        mViewPager.setCurrentItem(curr);

    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private final int[] guideArray = {R.array.emergency_react_guide_1, R.array.emergency_react_guide_2, R.array.emergency_react_guide_3};
        private final int[] guideTitles = {R.string.emergency_react_guide_title_1, R.string.emergency_react_guide_title_2, R.string.emergency_react_guide_title_3};
        ListView emGuideListView;
        String[] emGuideStringList;
        ArrayList<String> emGuideList;
        ListAdapter emGuideListAdap;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_emergency, container, false);

            TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            tvTitle.setText(getResources().getString(guideTitles[getArguments().getInt(ARG_SECTION_NUMBER) - 1]));

            emGuideListView = (ListView) rootView.findViewById(R.id.emguide_list_view);
            emGuideStringList = getResources().getStringArray(guideArray[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            emGuideList = new ArrayList<String>();
            emGuideList.addAll(Arrays.asList(emGuideStringList));
            emGuideListAdap = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.bullet_list_row, emGuideList);
            emGuideListView.setAdapter(emGuideListAdap);


            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
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
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
