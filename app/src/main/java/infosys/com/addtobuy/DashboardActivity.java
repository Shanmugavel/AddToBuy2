package infosys.com.addtobuy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import infosys.com.addtobuy.infosys.com.addtobuy.util.ExpandableListAdapter;


public class DashboardActivity extends ActionBarActivity implements ActionBar.TabListener {

    private static final String TAG = "MyApp";



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
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
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (0 == position) {
               return FeedsFragment.newInstance(position + 1);
            } else if (1 == position) {
                return MyPlansFragment.newInstance(position + 1);
            } else {
                return SearchFragment.newInstance(position + 2);
            }
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FeedsFragment extends Fragment {


        ExpandableListAdapter feedExplLstAdpater;
        ExpandableListView feedExpLstView;
        private List<String> feedlstHeader;
        private Map<String, List<String>> feedlstData;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FeedsFragment newInstance(int sectionNumber) {
            FeedsFragment fragment = new FeedsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public FeedsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

            feedExpLstView = (ExpandableListView) rootView.findViewById(R.id.dashboardList);

          feedExpLstView.setOnChildClickListener( new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Log.d(TAG, "groupPosition=" + groupPosition + ";childPosition=" + childPosition + ";id=" + id);
                    //My Offers
                    if (0 == groupPosition) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.individual_offer_dialog);
                        dialog.setTitle("Offer");

                        Button addToPlnBtn = (Button) dialog.findViewById(R.id.addToPlnBtn);
                        // if button is clicked, close the custom dialog
                        addToPlnBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                final CharSequence[] items = {
                                        "Next Week", "Next Month", "Just Wish!!!"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Make your selection");
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        Log.i(TAG, "Chosen!!");
                                        // Do something with the selection
                                        //Toast.makeText(getActivity().getApplicationContext(), "Thank you!, you have chosen " + items[item], Toast.LENGTH_LONG);
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        });

                        Button buyNowBtn = (Button) dialog.findViewById(R.id.buyNowBtn);
                        // if button is clicked, close the custom dialog
                        buyNowBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                        return true;
                    } else {//New Offers

                    }

                    return false;
                }
            });
             /* feedExpLstView.setOnGroupClickListener( new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Log.d(TAG, "groupPosition="+groupPosition+";id="+id);
                    if (Constant.CONST_ANNOUNCEMENTS == groupPosition) {
                        Log.d(TAG, "All Announcement is clicked!!!");
                        Intent i = new Intent(getApplicationContext(), AllAnnouncementsActivity.class);
                        startActivity(i);
                        //overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_left);
                        return true;
                    }
                    return false;
                }
            });*/

            // preparing list data
            prepareListData();

            feedExplLstAdpater = new ExpandableListAdapter(getActivity().getApplicationContext(), feedlstHeader, feedlstData);

            // setting list adapter
            feedExpLstView.setAdapter(feedExplLstAdpater);

            feedExpLstView.expandGroup(0);
            feedExpLstView.expandGroup(1);

            //TextView txt = (TextView) rootView.findViewById(R.id.feeds_section_label);
            //txt.setText("Dummy!!");
            return rootView;
        }

        public void prepareListData() {
            feedlstHeader = new ArrayList<String>();
            feedlstData = new HashMap<String, List<String>>();

            String myOffers = "My Offers";
            String newOffers = "New Offers";
            feedlstHeader.add(myOffers);
            feedlstHeader.add(newOffers);

            List<String> newOffersDataLst = new ArrayList<String>();
            List<String> myOffersDataLst = new ArrayList<String>();

            newOffersDataLst.add("Shoe - New Offer from Kohl's");
            newOffersDataLst.add("T-Shirt - New Offer From Target");

            myOffersDataLst.add("LED TV - New quote from Walmart");
            myOffersDataLst.add("MacBook Pro - New Quote form Amazon");
            myOffersDataLst.add("iPhone - New Quote from Radioshack");

            feedlstData.put(myOffers, myOffersDataLst);
            feedlstData.put(newOffers, newOffersDataLst);
            // Map<String, String> urlAnnmnts = new HashMap<String, String>();
            //urlAnnmnts.put(Constant.URL, getString(R.string.SVC_FETCH_ALL_ANNOUNCEMENTS));


            //new FetchAllAnnouncementsHelper(this).execute(urlAnnmnts);

        }
    }

    public static class MyPlansFragment extends Fragment {

        ExpandableListAdapter myPlansExplLstAdpater;
        ExpandableListView myPlansExpLstView;
        private List<String> myPlanslstHeader;
        private Map<String, List<String>> myPlanslstData;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MyPlansFragment newInstance(int sectionNumber) {
            MyPlansFragment fragment = new MyPlansFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MyPlansFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_myplans, container, false);

            myPlansExpLstView = (ExpandableListView) rootView.findViewById(R.id.myPlansDashboardList);

            // preparing list data
            prepareListData();

            myPlansExplLstAdpater = new ExpandableListAdapter(getActivity().getApplicationContext(), myPlanslstHeader, myPlanslstData);

            // setting list adapter
            myPlansExpLstView.setAdapter(myPlansExplLstAdpater);


            myPlansExpLstView.expandGroup(0);
            myPlansExpLstView.expandGroup(1);
            myPlansExpLstView.expandGroup(2);

            return rootView;
        }


        public void prepareListData() {
            myPlanslstHeader = new ArrayList<String>();
            myPlanslstData = new HashMap<String, List<String>>();

            String nxtWeek = "Next Week";
            String nxtMth = "Next Month";
            String jstWish = "Just Wish";

            myPlanslstHeader.add(nxtWeek);
            myPlanslstHeader.add(nxtMth);
            myPlanslstHeader.add(jstWish);

            List<String> nxtWeekDataLst = new ArrayList<String>();
            List<String> nxtMthDataLst = new ArrayList<String>();
            List<String> jstWishDataLst = new ArrayList<String>();

            nxtWeekDataLst.add("T-Shirt - New Offer from Kohl's <<Competing Offer Received>>");
            nxtWeekDataLst.add("3 Movie DVD - New Offer From Target");

            nxtMthDataLst.add("Shoe - New Offer from Walmart <<Competing Offer Received>>");
            nxtMthDataLst.add("Kindle Fire - New Offer form Amazon");
            nxtMthDataLst.add("jLab Tab - New Offer from Oracle");

            jstWishDataLst.add("Diamond Ring - New Offer from Macy's");


            myPlanslstData.put(nxtWeek, nxtWeekDataLst);
            myPlanslstData.put(nxtMth, nxtMthDataLst);
            myPlanslstData.put(jstWish, jstWishDataLst);
            // Map<String, String> urlAnnmnts = new HashMap<String, String>();
            //urlAnnmnts.put(Constant.URL, getString(R.string.SVC_FETCH_ALL_ANNOUNCEMENTS));


            //new FetchAllAnnouncementsHelper(this).execute(urlAnnmnts);

        }
    }

    public static class SearchFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SearchFragment newInstance(int sectionNumber) {
            SearchFragment fragment = new SearchFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SearchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            return rootView;
        }
    }
}
