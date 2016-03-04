package barqsoft.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.R;
import barqsoft.footballscores.data.DatabaseContract;
import barqsoft.footballscores.utils.Utils;

public class WidgetService extends RemoteViewsService {
    private final String DATE_FORMAT = "yyyy-MM-dd";

    public static final int COL_MATCH_TIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;

/*
* So pretty simple just defining the Adapter of the listview
* here Adapter is ListProvider
* */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext()));
    }

    /**
     * If you are familiar with Adapter of ListView,this is the same as adapter
     * with few changes
     */
    public class ListProvider implements RemoteViewsService.RemoteViewsFactory{ //, Loader.OnLoadCompleteListener<Cursor> {
        private List items = new ArrayList();
        private Context context = null;
        public ListProvider(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
            final long token = Binder.clearCallingIdentity();
            try {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

                Cursor cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreForWidget(), null, null, new String[]{formatter.format(date)}, null);
                if (cursor.getCount()>0){
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        items.add(getContent(cursor));
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            } finally {
                Binder.restoreCallingIdentity(token);
            }

        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        /*
        *Similar to getView of Adapter where instead of View
        *we return RemoteViews
        *
        */
        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            WidgetMatchItem widgetMatchItem = (WidgetMatchItem) items.get(position);
            remoteView.setTextViewText(R.id.home_name, widgetMatchItem.homeName);
            remoteView.setTextViewText(R.id.away_name, widgetMatchItem.awayName);
            remoteView.setTextViewText(R.id.data_textview, widgetMatchItem.date);
            remoteView.setTextViewText(R.id.score_textview, widgetMatchItem.score);
            remoteView.setImageViewResource(R.id.home_crest, widgetMatchItem.homeCrest);
            remoteView.setImageViewResource(R.id.away_crest, widgetMatchItem.awayCrest);

            remoteView.setInt(R.id.rootView, "setBackgroundColor", android.graphics.Color.BLACK);
            final Intent fillInIntent = new Intent();
            fillInIntent.setAction(WidgetProvider.ACTION_SCORE);
            final Bundle bundle = new Bundle();
            bundle.putDouble(WidgetProvider.EXTRA_MATCHID, widgetMatchItem.matchId);
            fillInIntent.putExtras(bundle);
            remoteView.setOnClickFillInIntent(R.id.rootView, fillInIntent);
            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }
    }

    private WidgetMatchItem getContent(Cursor cursor) {
        WidgetMatchItem content = new WidgetMatchItem();
        content.homeName = cursor.getString(COL_HOME);
        content.awayName = cursor.getString(COL_AWAY);
        content.date = cursor.getString(COL_MATCH_TIME);
        content.score = Utils.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS));
        content.matchId = cursor.getDouble(COL_ID);
        content.homeCrest = Utils.getTeamCrestByTeamName(cursor.getString(COL_HOME));
        content.awayCrest = Utils.getTeamCrestByTeamName(cursor.getString(COL_AWAY));
        return content;
    }
}