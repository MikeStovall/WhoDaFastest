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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatsActivity extends AppCompatActivity {
    private final static int player1 = 1;
    private final static int player2 = 2;
    private final static int player3 = 3;
    private final static int player4 = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // load and display ReactionTimer stats
        displayReactionTimerStats();

        // load and display GameShow stats

        // Hook up Erase button
        Button btnErase = (Button) findViewById(R.id.btnErase);
        btnErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO present dialog confirming this destructive action
                StatsModel.getStatsModel().clear(getApplicationContext());
                displayReactionTimerStats();
            }
        });

        // Hook up Email button
        Button btnEmail = (Button) findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mike@mikestovall.com"});
                String date = new SimpleDateFormat().format(new Date());
                i.putExtra(Intent.EXTRA_SUBJECT, String.format("reflex Statistics [%s]", date));
                i.putExtra(Intent.EXTRA_TEXT, StatsModel.getStatsModel().getEmailBody(getApplicationContext()));
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(StatsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void displayReactionTimerStats() {


        StatsModel stats1 = StatsModel.getStatsModel();
        stats1.setFilename(player1);
         ArrayList<Integer> resourceIds1 = new ArrayList<Integer>() {{
            add(R.id.player1Avg);
            add(R.id.player1Rnd1);
            add(R.id.player1Rnd2);
            add(R.id.player1Rnd3);
            add(R.id.player1Rnd4);
            add(R.id.player1Rnd5);
        }};
         ArrayList<StatType> statTypes = new ArrayList<StatType>() {{
            add(StatType.AVERAGE);
            add(StatType.ROUND1);
            add(StatType.ROUND2);
            add(StatType.ROUND3);
            add(StatType.ROUND4);
            add(StatType.ROUND5);
        }};
        int resCount1 = 0;
        TextView tv1;
        for (int i=0; i < resourceIds1.size(); i++) {
                 tv1 = (TextView) findViewById(resourceIds1.get(resCount1));
                Long stat = stats1.getReactionTimeStat(statTypes.get(i), 0, getApplicationContext());
                tv1.setText(String.valueOf(stat));
                resCount1++;
            }

        StatsModel stats2 = StatsModel.getStatsModel();
        stats2.setFilename(player2);
         ArrayList<Integer> resourceIds2 = new ArrayList<Integer>() {{
            add(R.id.player2Avg);
            add(R.id.player2Rnd1);
            add(R.id.player2Rnd2);
            add(R.id.player2Rnd3);
            add(R.id.player2Rnd4);
            add(R.id.player2Rnd5);
        }};
        int resCount2 = 0;
        TextView tv2;
        for (int i=0; i < resourceIds2.size(); i++) {
                 tv2 = (TextView) findViewById(resourceIds2.get(resCount2));
                Long stat = stats2.getReactionTimeStat(statTypes.get(i), 0, getApplicationContext());
                tv2.setText(String.valueOf(stat));
                resCount2++;
            }


        StatsModel stats3 = StatsModel.getStatsModel();
        stats3.setFilename(player3);
         ArrayList<Integer> resourceIds3 = new ArrayList<Integer>() {{
            add(R.id.player3Avg);
            add(R.id.player3Rnd1);
            add(R.id.player3Rnd2);
            add(R.id.player3Rnd3);
            add(R.id.player3Rnd4);
            add(R.id.player3Rnd5);
        }};
        int resCount3 = 0;
        TextView tv3;
        for (int i=0; i < resourceIds3.size(); i++) {
                 tv3 = (TextView) findViewById(resourceIds3.get(resCount3));
                Long stat = stats3.getReactionTimeStat(statTypes.get(i), 0, getApplicationContext());
                tv3.setText(String.valueOf(stat));
                resCount3++;
            }


        StatsModel stats4 = StatsModel.getStatsModel();
        stats4.setFilename(player4);
         ArrayList<Integer> resourceIds4 = new ArrayList<Integer>() {{
            add(R.id.player4Avg);
            add(R.id.player4Rnd1);
            add(R.id.player4Rnd2);
            add(R.id.player4Rnd3);
            add(R.id.player4Rnd4);
            add(R.id.player4Rnd5);
        }};
        int resCount4 = 0;
        TextView tv4;
        for (int i=0; i < resourceIds4.size(); i++) {
                 tv4 = (TextView) findViewById(resourceIds4.get(resCount4));
                Long stat = stats4.getReactionTimeStat(statTypes.get(i), 0, getApplicationContext());
                tv4.setText(String.valueOf(stat));
                resCount4++;
            }

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
