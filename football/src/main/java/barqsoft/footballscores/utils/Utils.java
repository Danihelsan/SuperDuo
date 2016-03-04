package barqsoft.footballscores.utils;

import barqsoft.footballscores.R;

/**
 * Created byÒ yehya khaled on 3/3/2015.
 */
public class Utils {
    public static final int
            BUNDESLIGA = 351,
            PREMIER_LEAGUE = 354,
            FANTASY_CHAMPIONSHIP = 355,
            SERIE_A = 357,
            PRIMERA_DIVISION = 358,
            CHAMPIONS_LEAGUE = 362;

    public static int getLeague(int leagueCode) {
        switch (leagueCode) {
            case SERIE_A:
                return R.string.seriaa;
            case PREMIER_LEAGUE:
                return R.string.premierleague;
            case FANTASY_CHAMPIONSHIP:
                return R.string.fantasy_championship;
            case CHAMPIONS_LEAGUE:
                return R.string.champions_league;
            case PRIMERA_DIVISION:
                return R.string.primeradivison;
            case BUNDESLIGA:
                return R.string.bundesliga;
            default:
                return R.string.unknown;
        }
    }

    public static int getMatchDay(int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return R.string.group_stage_text;
            } else if (match_day == 7 || match_day == 8) {
                return R.string.first_knockout_round;
            } else if (match_day == 9 || match_day == 10) {
                return R.string.quarter_final;
            } else if (match_day == 11 || match_day == 12) {
                return R.string.semi_final;
            } else {
                return R.string.final_text;
            }
        } else {
            return R.string.match_nro;
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamName) {
        if (teamName == null) {
            return R.drawable.no_icon;
        }
        switch (teamName) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Swansea City":
                return R.drawable.swansea_city_afc;
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            default:
                return R.drawable.no_icon;
        }
    }
}
