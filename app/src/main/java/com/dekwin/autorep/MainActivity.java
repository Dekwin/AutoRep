package com.dekwin.autorep;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dekwin.autorep.db.DatabaseHelper;
import com.dekwin.autorep.entities.Work;
import com.dekwin.autorep.fragments.AddWorksFragment;
import com.dekwin.autorep.fragments.ContractsFragment;
import com.dekwin.autorep.fragments.OrganizationsFragment;
import com.dekwin.autorep.fragments.ResponsibleFragment;
import com.dekwin.autorep.fragments.SparesFragment;
import com.dekwin.autorep.fragments.WorksFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, AddWorksFragment.FragmentIntListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.deleteDatabase("repair_db.sqlite");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        DatabaseHelper.getInstance(this);


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();


        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }


        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNavigationDrawerFragment.onConfigurationChanged(newConfig);
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public void getWorksList(List<Work> works) {

        FragmentManager fragmentManager = getSupportFragmentManager();



                if(fragmentManager.findFragmentById(R.id.container) instanceof SparesFragment){
                    SparesFragment frag=(SparesFragment) fragmentManager.findFragmentById(R.id.container);
                    frag.getWorksIdFromDialog(works);
                }else{

                    ContractsFragment frag = (ContractsFragment) fragmentManager.findFragmentById(R.id.container);
                    frag.getWorksIdFromDialog(works);

                }


    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            Log.e("super", "sect " + sectionNumber);
            Fragment fragment = null;
            switch (sectionNumber) {
                case 1:
                    fragment = new SparesFragment();
                    break;
                case 2:
                    fragment = new ResponsibleFragment();
                    break;
                case 3:
                    fragment = new OrganizationsFragment();
                    break;
                case 4:
                    fragment = new WorksFragment();
                    break;
                case 5:
                    fragment = new ContractsFragment();
                    break;
                default:
                    fragment = new WorksFragment();
                    break;
            }

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        private ListView tbl;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.spare_info, container, false);
            //tbl = (ListView) findViewById(R.id.spares_list1);

            tbl = (ListView) rootView.findViewById(R.id.spares_list);

            if (tbl == null)
                Log.e("first", "null");


            return rootView;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
