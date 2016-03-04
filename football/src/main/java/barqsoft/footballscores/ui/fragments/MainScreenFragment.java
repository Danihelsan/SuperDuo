package barqsoft.footballscores.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ui.adapters.ScoresAdapter;
import barqsoft.footballscores.service.FootBallFetchService;
import barqsoft.footballscores.ui.activities.MainActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String KEY_DATE = "key_date";
    public ScoresAdapter adapter;
    public static final int SCORES_LOADER = 0;
    private String[] fragmentdate;

    public static MainScreenFragment newInstance(String date) {
        MainScreenFragment fragment = new MainScreenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_DATE,date);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentdate = new String[1];
        fragmentdate[0] = this.getArguments().getString(KEY_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView scoreList = (ListView) rootView.findViewById(R.id.scores_list);
        adapter = new ScoresAdapter(getActivity(), null, 0);
        scoreList.setAdapter(adapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        adapter.detail_match_id = MainActivity.selected_match_id;
        scoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScoresAdapter.ViewHolder selected = (ScoresAdapter.ViewHolder) view.getTag();
                adapter.detail_match_id = selected.getMatch_id();
                MainActivity.selected_match_id = (int) selected.getMatch_id();
                adapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(), null, null, fragmentdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cursor.moveToNext();
        }
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    private void update_scores() {
        Intent service_start = new Intent(getActivity(), FootBallFetchService.class);
        getActivity().startService(service_start);
    }
}
