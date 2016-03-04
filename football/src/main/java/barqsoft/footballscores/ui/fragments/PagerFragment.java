package barqsoft.footballscores.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.activities.MainActivity;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment {
    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final String DAY_FORMAT = "EEEE";
    public static final int NUM_PAGES = 5;
    public ViewPager pager;
    private ScorePageAdapter adapter;
    private MainScreenFragment[] viewFragments = new MainScreenFragment[5];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        adapter = new ScorePageAdapter(getChildFragmentManager());
        for (int i = 0; i < NUM_PAGES; i++) {
            Date date = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            viewFragments[i] = MainScreenFragment.newInstance(formatter.format(date));
        }
        pager.setAdapter(adapter);
        pager.setCurrentItem(MainActivity.current_fragment);
        return rootView;
    }

    private class ScorePageAdapter extends FragmentStatePagerAdapter {
        @Override
        public Fragment getItem(int i) {
            return viewFragments[i];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public ScorePageAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }

        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.

            Time t = new Time();
            t.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);
            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);
            } else {
                Time time = new Time();
                time.setToNow();
                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat(DAY_FORMAT);
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
