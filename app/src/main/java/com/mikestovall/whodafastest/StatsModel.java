/*
 * Who's Fastest? The Search for the World's Fastest Humans
 * Copyright (C) 2017 Mike Stovall
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mikestovall.whodafastest;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mstovall on 17-01-20.
 */
public class StatsModel {
    private String REACTION_TIMES_FILENAME = "statsRT1.json";

    public static StatsModel instance;

    public StatsModel() { }
    public static StatsModel getStatsModel() {
        if (instance == null) {
            instance = new StatsModel();
        }
        return instance;
    }

    public void setFilename(int playernum){
        switch (playernum){
            case GameActivity.player1:
                REACTION_TIMES_FILENAME = "statsRT1.json";
                break;
            case GameActivity.player2:
                REACTION_TIMES_FILENAME = "statsRT2.json";
                break;
             case GameActivity.player3:
                REACTION_TIMES_FILENAME = "statsRT3.json";
                break;
            case GameActivity.player4:
                REACTION_TIMES_FILENAME = "statsRT4.json";
                break;
            default:
                 REACTION_TIMES_FILENAME = "statsRT1.json";
                break;
        }
    }
    // Calculates the given StatType on the most recent n results
    // N = -1 implies "All Time"
    public Long getReactionTimeStat(StatType statType, int N, Context cxt) {
        ArrayList<Long> times = readReactionTimesFromFile(cxt);
        List<Long> list;
//        if (N < 0) {
            list = (List) times;
//        if (list.size() <= 4) {
//            return 0L;
//        }

//        Collections.sort(list);
        Long result = -1L;
        switch (statType) {
            case AVERAGE:
                Long sum = 0L;
                Long time = 0L;
                if(list.size() >= 5){
                    for (int i = 0; i <= 4 ; i++) {
                        time = list.get(i);
                        if(time == 0) {
                            return 0L;
                        }
                        sum += time;
                    }
                    result = sum / 5;
                } else
                    return 0L;
                break;
            case ROUND1:
                if(list.size() >= 1){
                 result = list.get(0);
                } else
                result = 0L;
                break;
            case ROUND2:
                if(list.size() >= 2){
                 result = list.get(1);
                } else
                result = 0L;
                break;
            case ROUND3:
                if(list.size() >= 3){
                 result = list.get(2);
                } else
                result = 0L;
                break;
            case ROUND4:
                 if(list.size() >= 4){
                 result = list.get(3);
                } else
                result = 0L;
                break;
            case ROUND5:
                 if(list.size() >= 5){
                 result = list.get(4);
                } else
                result = 0L;
                break;
            default:{
                break;

            }
        }

        return result;
    }

    public void saveReactionTime(Long elapsed, Context cxt) {
        ArrayList<Long> times = readReactionTimesFromFile(cxt);
        times.add(0, elapsed);
        writeReactionTimesToFile(times, cxt);
    }

    public void clear(Context cxt){

        setFilename(GameActivity.player1);
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);
         setFilename(GameActivity.player2);
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);
        setFilename(GameActivity.player3);
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);
        setFilename(GameActivity.player4);
        writeReactionTimesToFile(new ArrayList<Long>(), cxt);
   }

    public String getEmailBody(Context cxt) {
        String body = "";
        body += "Reaction Timer\t Avg\tRnd1\tRnd2\tRnd3\tRnd4\tRnd5\n";
        setFilename(GameActivity.player1);

        body += String.format("Player1 Results\t%d\t%d\t%d\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.AVERAGE,0 , cxt),
                getReactionTimeStat(StatType.ROUND1, 0, cxt),
                getReactionTimeStat(StatType.ROUND2, 0, cxt),
                getReactionTimeStat(StatType.ROUND3, 0, cxt),
                getReactionTimeStat(StatType.ROUND4, 0, cxt),
                getReactionTimeStat(StatType.ROUND5, 0, cxt));
        setFilename(GameActivity.player2);
        body += String.format("Player2 Results\t%d\t%d\t%d\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.AVERAGE,0 , cxt),
                getReactionTimeStat(StatType.ROUND1, 0, cxt),
                getReactionTimeStat(StatType.ROUND2, 0, cxt),
                getReactionTimeStat(StatType.ROUND3, 0, cxt),
                getReactionTimeStat(StatType.ROUND4, 0, cxt),
                getReactionTimeStat(StatType.ROUND5, 0, cxt));
        setFilename(GameActivity.player3);
        body += String.format("Player3 Results\t%d\t%d\t%d\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.AVERAGE,0 , cxt),
                getReactionTimeStat(StatType.ROUND1, 0, cxt),
                getReactionTimeStat(StatType.ROUND2, 0, cxt),
                getReactionTimeStat(StatType.ROUND3, 0, cxt),
                getReactionTimeStat(StatType.ROUND4, 0, cxt),
                getReactionTimeStat(StatType.ROUND5, 0, cxt));
        setFilename(GameActivity.player4);
        body += String.format("Player4 Results\t%d\t%d\t%d\t%d\t%d\t%d\n",
                getReactionTimeStat(StatType.AVERAGE,0 , cxt),
                getReactionTimeStat(StatType.ROUND1, 0, cxt),
                getReactionTimeStat(StatType.ROUND2, 0, cxt),
                getReactionTimeStat(StatType.ROUND3, 0, cxt),
                getReactionTimeStat(StatType.ROUND4, 0, cxt),
                getReactionTimeStat(StatType.ROUND5, 0, cxt));
        body += "\n";
        return body;
    }

    private ArrayList<Long> readReactionTimesFromFile(Context cxt) {
        ArrayList<Long> times = null;
        Gson gson = new Gson();
        try {
            FileInputStream fis = new FileInputStream(getReactionTimesPath(cxt));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            Type type = new TypeToken<ArrayList<Long>>() {}.getType();
            times = gson.fromJson(reader, type);
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e("Stats", String.format("File not found at '{}'", getReactionTimesPath(cxt)));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (times == null) {
            times = new ArrayList<Long>();
        }
        return times;
    }
    private void writeReactionTimesToFile(ArrayList<Long> times, Context cxt) {
        Gson gson = new Gson();
        try {
            FileOutputStream fos = new FileOutputStream(getReactionTimesPath(cxt));
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(times, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getBasePath(Context cxt) {
        return cxt.getFilesDir().getPath().concat("/");
    }
    private String getReactionTimesPath(Context cxt) {
        return getBasePath(cxt).concat(REACTION_TIMES_FILENAME);
    }
}
