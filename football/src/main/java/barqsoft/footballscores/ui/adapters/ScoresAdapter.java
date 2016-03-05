package barqsoft.footballscores.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.utils.Utils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCH_DAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCH_TIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View item = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder holder = new ViewHolder(item);
        item.setTag(holder);
        return item;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.home_name.setText(cursor.getString(COL_HOME));
        holder.away_name.setText(cursor.getString(COL_AWAY));
        holder.date.setText(cursor.getString(COL_MATCH_TIME));
        holder.score.setText(Utils.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        holder.match_id = cursor.getDouble(COL_ID);
        holder.home_crest.setImageResource(Utils.getTeamCrestByTeamName(cursor.getString(COL_HOME)));
        holder.away_crest.setImageResource(Utils.getTeamCrestByTeamName(cursor.getString(COL_AWAY)));
        holder.home_crest.setContentDescription(cursor.getString(COL_HOME));
        holder.away_crest.setContentDescription(cursor.getString(COL_AWAY));

        LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if (holder.match_id == detail_match_id) {
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            int matchDayResId = Utils.getMatchDay(cursor.getInt(COL_MATCH_DAY), cursor.getInt(COL_LEAGUE));
            if (matchDayResId == R.string.match_nro){
                String matchDay = context.getString(matchDayResId, String.valueOf(cursor.getInt(COL_MATCH_DAY)));
                match_day.setText(matchDay);
            } else{
                match_day.setText(matchDayResId);
            }

            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utils.getLeague(cursor.getInt(COL_LEAGUE)));

            Button share_button = (Button) v.findViewById(R.id.share_button);
            if (cursor.getInt(COL_HOME_GOALS)>=0 && cursor.getInt(COL_HOME_GOALS)>=0){
                share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(createShareForecastIntent(holder.home_name.getText() + " " + holder.score.getText() + " " + holder.away_name.getText() + " "));
                    }
                });
                share_button.setContentDescription(context.getString(R.string.shareMatchCD,cursor.getString(COL_HOME),cursor.getString(COL_AWAY)));
            } else{
                share_button.setVisibility(View.GONE);
            }
        } else {
            container.removeAllViews();
        }

    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

    public class ViewHolder {

        @Bind(R.id.home_name)
        TextView home_name;
        @Bind(R.id.away_name)
        TextView away_name;
        @Bind(R.id.score_textview)
        TextView score;
        @Bind(R.id.data_textview)
        TextView date;
        @Bind(R.id.home_crest)
        ImageView home_crest;
        @Bind(R.id.away_crest)
        ImageView away_crest;
        private double match_id;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public double getMatch_id() {
            return match_id;
        }
    }

}
