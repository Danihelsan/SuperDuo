package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {
/*
* So pretty simple just defining the Adapter of the listview
* here Adapter is ListProvider
* */

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

    /**
     * If you are familiar with Adapter of ListView,this is the same as adapter
     * with few changes
     */
    public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
        private ArrayList listItemList = new ArrayList();
        private Context context = null;
        private int appWidgetId;

        public ListProvider(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            populateListItem();
        }

        private void populateListItem() {
            for (int i = 0; i < 10 ;i++){
                ListItem listItem = new ListItem();
                listItem.heading = "Heading" + i;
                listItem.content = i + " This is the content of the app widget listview.Nice content though";
                listItemList.add(listItem);
            }

        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {}

        @Override
        public void onDestroy() {}

        @Override
        public int getCount() {
            return listItemList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /*
        *Similar to getView of Adapter where instead of View
        *we return RemoteViews
        *
        */
        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            ListItem listItem = (ListItem) listItemList.get(position);
            remoteView.setTextViewText(R.id.heading, listItem.heading);
            remoteView.setTextViewText(R.id.content, listItem.content);

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
}