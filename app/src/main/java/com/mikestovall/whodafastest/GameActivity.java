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

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
//import android.os.Debug;
import android.view.Menu;
import android.view.MenuItem;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.SoundPool.OnLoadCompleteListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.lang.String;



public class GameActivity extends AppCompatActivity
        implements ReactionTimerInfoDialogFragment.DialogListener {
    // cms
    private ReactionTimer reactionTimer;
    private static boolean first = false;
    private static boolean second = false;
    private static boolean third = false;
    private static boolean fourth = false;
    private static boolean play1 = false;
    private static boolean play2 = false;
    private static boolean play3 = false;
    private static boolean play4 = false;
    public final static int player1 = 1;
    public final static int player2 = 2;
    public final static int player3 = 3;
    public final static int player4 = 4;
    private static long player1Score = 0;
    private static long player2Score = 0;
    private static long player3Score = 0;
    private static long player4Score = 0;
    private static int winner = 0;
    private static int dummy = 0;
    private static boolean firstbad = false;
//    private long startTimer = 0;
//    private long startTimer1 = 0;
    private static String playerFirst;
    private static String playerSecond;
    private static String playerThird;
    private static String playerFourth;
	private SoundPool soundPool;
	private int soundID;
	private boolean loaded = false;

    private Handler handler;
    public enum Statetype {
        STOP,
        READY,
        GO,
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    private Statetype gameState;

//    private GameShow gameShow;
    int[] layoutIds = {-1, -1,
            R.layout.game_show_4player};
    ArrayList<Integer> buttonIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // default 2 players cms
        setContentView(getLayoutInflater().inflate(R.layout.game_show_4player, null));

//        gameShow = new GameShow();
//        gameShow.setNumPlayers(4); // default 4 players cms
        buttonIds = new ArrayList<Integer>() {{
            add(R.id.imageButton1);
            add(R.id.imageButton2);
            add(R.id.imageButton3);
            add(R.id.imageButton4);
            add(R.id.imageRedLight); // RED
            add(R.id.imageYellowLight); // YELLOW
            add(R.id.imageGreenLight); // GREEN

        }};

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId,
              int status) {
              loaded = true;
              }

        });
		soundID = soundPool.load(this, R.raw.hangouts_message, 1);



        // present info dialog
        DialogFragment infoDialog = new ReactionTimerInfoDialogFragment();
        infoDialog.show(getFragmentManager(), "info");


        this.reactionTimer = new ReactionTimer() {
            @Override
            void onStart() {
                first = second = third = fourth = firstbad = false;
                player1Score = player2Score = player3Score = player4Score = 0;
                winner = 0;
                gameState = Statetype.STOP;
                findViewById(R.id.imageRedLight).setVisibility(View.VISIBLE);
                findViewById(R.id.imageYellowLight).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageGreenLight).setVisibility(View.INVISIBLE);
            }

            @Override
            public void displayYellow() {
                gameState = Statetype.READY;
                findViewById(R.id.imageRedLight).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageYellowLight).setVisibility(View.VISIBLE);
                findViewById(R.id.imageGreenLight).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {
                 gameState = Statetype.GO;
               findViewById(R.id.imageRedLight).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageYellowLight).setVisibility(View.INVISIBLE);
                findViewById(R.id.imageGreenLight).setVisibility(View.VISIBLE);


            }
            @Override
            public void displayScores() {
                 gameState = Statetype.STOP;

                if (playerFirst != null) {

//                    Toast.makeText(activity, playerFirst, Toast.LENGTH_SHORT).setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0,0);
                    Toast.makeText(activity, playerFirst, Toast.LENGTH_SHORT).show();
//                    reactionTimer.saveReactionTime(p1Score, getApplicationContext());
                    playerFirst = null;
                }
                if (playerSecond != null) {

                    Toast.makeText(activity, playerSecond, Toast.LENGTH_SHORT).show();
//                    reactionTimer.saveReactionTime(p2Score, getApplicationContext());
                    playerSecond = null;
                }
                if (playerThird != null) {

                    Toast.makeText(activity, playerThird, Toast.LENGTH_SHORT).show();
//                    reactionTimer.saveReactionTime(p3Score, getApplicationContext());
                    playerThird = null;
                }
                if (playerFourth != null) {

                    Toast.makeText(activity, playerFourth, Toast.LENGTH_SHORT).show();
//                    reactionTimer.saveReactionTime(p4Score, getApplicationContext());
                    playerFourth = null;
                }
                 TextView tv3;
                if(player1Score != 0){
                    StatsModel stats = StatsModel.getStatsModel();
                    stats.setFilename(player1);
                    reactionTimer.saveReactionTime(player1Score, getApplicationContext());
                    tv3 = (TextView) findViewById(R.id.textViewPlayer1ScoreV);
                    tv3.setText(String.valueOf(player1Score).concat(" msec"));
                }
                 if(player2Score != 0){
                        StatsModel stats = StatsModel.getStatsModel();
                        stats.setFilename(player2);
                    reactionTimer.saveReactionTime(player2Score, getApplicationContext());

                 tv3 = (TextView) findViewById(R.id.textViewPlayer2ScoreV);
                 tv3.setText(String.valueOf(player2Score).concat(" msec"));
                }
                if(player3Score != 0){
                        StatsModel stats = StatsModel.getStatsModel();
                        stats.setFilename(player3);
                    reactionTimer.saveReactionTime(player3Score, getApplicationContext());

                 tv3 = (TextView) findViewById(R.id.textViewPlayer3ScoreV);
                 tv3.setText(String.valueOf(player3Score).concat(" msec"));
                }
                if(player4Score != 0){
                        StatsModel stats = StatsModel.getStatsModel();
                        stats.setFilename(player4);
                    reactionTimer.saveReactionTime(player4Score, getApplicationContext());

                tv3 = (TextView) findViewById(R.id.textViewPlayer4ScoreV);
                 tv3.setText(String.valueOf(player4Score).concat(" msec"));
                }

            }
            @Override
            void onKill() {
                // TODO something?
            }
        };
    }


    final GameActivity activity = this;



        //
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_game_show, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
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

    public void onValueChange(int oldVal, int newVal) {
//        gameShow.setNumPlayers(newVal);
//        View v = getLayoutInflater().inflate(layoutIds[gameShow.getNumPlayers()], null);
        View v = getLayoutInflater().inflate(layoutIds[4], null);
        setContentView(v);
    }
    private void clearScores(){
        TextView tv3;
        tv3 = (TextView) findViewById(R.id.textViewPlayer1ScoreV);
        tv3.setText(String.valueOf(player1Score).concat(" msec"));
        tv3 = (TextView) findViewById(R.id.textViewPlayer2ScoreV);
        tv3.setText(String.valueOf(player2Score).concat(" msec"));
        tv3 = (TextView) findViewById(R.id.textViewPlayer3ScoreV);
        tv3.setText(String.valueOf(player3Score).concat(" msec"));
        tv3 = (TextView) findViewById(R.id.textViewPlayer4ScoreV);
        tv3.setText(String.valueOf(player4Score).concat(" msec"));
        tv3.showContextMenu();

    }

    public void startOver(View view) {

        firstbad = first = second = third = fourth = play1 = play2 = play3 = play4 = false;
        playerFirst = playerSecond = playerThird = playerFourth = null;
        winner = 0;
        player1Score = player2Score = player3Score = player4Score = 0;
        this.reactionTimer.kill();
        clearScores();
        long delay = 2000L;
        this.reactionTimer.startdelay(delay);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDialogDismiss() {
        firstbad = first = second = third = fourth = play1 = play2 = play3 = play4 = false;
        playerFirst = playerSecond = playerThird = playerFourth = null;
        winner = 0;
        player1Score = player2Score = player3Score = player4Score = 0;
        clearScores();
       long redDelay = 2000l;
        this.reactionTimer.startdelay(redDelay);
        // this.reactionTimer.start();
    }

    private void sounderror(){
			// Getting the user sound settings
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			float actualVolume = (float) audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = (float) audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualVolume / maxVolume;
			// Is the sound loaded already?
			if (loaded) {
				soundPool.play(soundID, volume, volume, 1, 0, 1f);
//				Log.e("Test", "Played sound");
			}

    }


    public void player1(View view) {
        Long elapsed;
        View myView = view;
        if (gameState == Statetype.GO) {
            if (!play1) {
                play1 = true;
                if (!first && !firstbad) {
                    first = true;
                    elapsed = activity.reactionTimer.getElapsed();
                    if (elapsed == null) {
                        // display error message
                        sounderror();
                        playerFirst = "Player 1 Too Early";
                        Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
                        firstbad = true;
                       gameState = Statetype.STOP;
                    } else {
                        // display reactionTime
                        elapsed = elapsed - 200L; // Take out display rendering time
                        if (elapsed <= 100) {
                            playerFirst = "Player 1 Too Early ";
                            elapsed = 0L;
                            sounderror();
                            return;
                        }
                        playerFirst = "Player 1 First " + String.valueOf(elapsed).concat(" ms");
                        player1Score = elapsed;
//                        StatsModel stats = StatsModel.getStatsModel();
//                        stats.setFilename(player1);
                    }
                } else if (!second && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player1Score = elapsed;
                    second = true;
                    playerSecond = "Player 1 Second " + String.valueOf(elapsed).concat(" ms");
                } else if (!third && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player1Score = elapsed;
                    third = true;
                    playerThird = "Player 1 Third " + String.valueOf(elapsed).concat(" ms");
                } else if (!fourth && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player1Score = elapsed;
                    fourth = true;
                    playerFourth = "Player 1 Fourth " + String.valueOf(elapsed).concat(" ms");
                } else {
                    if (dummy == 0) dummy = 1;
                }
            }
        } else {
            if(gameState == Statetype.READY){
                sounderror();
                playerFirst = "Player 1 Too Early ";
               Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
             }
            gameState = Statetype.STOP;
            startOver(myView);
        }
    }

    public void player2(View view) {
        Long elapsed;
        View myView = view;
        if (gameState == Statetype.GO) {
            if (!play2) {
                play2 = true;
                if (!first && !firstbad) {
                    first = true;
                    elapsed = activity.reactionTimer.getElapsed();
                    if (elapsed == null) {
                        // display error message
                        sounderror();
                        playerFirst = "Player 2 Too Early ";
                        Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
                       firstbad = true;
                        gameState = Statetype.STOP;
                   } else {
                        elapsed = elapsed - 200L; // Take out display rendering time
                        if(elapsed <= 100){
                            playerFirst = "Player 2 Too Early ";
                            elapsed = 0L;
                            sounderror();
                            return;
                        }
                        // display reactionTime
                        playerFirst = "Player 2 First " + String.valueOf(elapsed).concat(" ms");
                        player2Score = elapsed;
                        // persist reactionDelay
//                         StatsModel stats = StatsModel.getStatsModel();
//                        stats.setFilename(player2);
                   }
                } else if (!second && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    second = true;
                    player2Score = elapsed;
                    playerSecond = "Player 2 Second " + String.valueOf(elapsed).concat(" ms");
                } else if (!third && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player2Score = elapsed;
                    third = true;
                    playerThird = "Player 2 Third " + String.valueOf(elapsed).concat(" ms");
                } else if (!fourth && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player2Score = elapsed;
                    fourth = true;
                    playerFourth = "Player 2 Fourth " + String.valueOf(elapsed).concat(" ms");
                } else {
                    dummy = 2;
                }
            }
        } else{
            if(gameState == Statetype.READY)
            {
                sounderror();
                playerFirst = "Player 2 Too Early";
                Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
            }
            gameState = Statetype.STOP;
            startOver(myView);
        }
    }
    public void player3(View view) {
        Long elapsed;
        View myView = view;
        if (gameState == Statetype.GO) {
            if (!play3) {
                play3 = true;
                if (!first && !firstbad) {
                    first = true;
                    elapsed = activity.reactionTimer.getElapsed();
                    if (elapsed == null) {
                        // display error message
                        sounderror();
                        playerFirst = "Player 3 Too Early ";
                        Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
                        firstbad = true;
                        gameState = Statetype.STOP;
                    } else {
                        elapsed = elapsed - 200L; // Take out display rendering time
                        if(elapsed <= 100){
                            playerFirst = "Player 3 Too Early ";
                            elapsed = 0L;
                            sounderror();
                            return;
                        }
                        player3Score = elapsed;
                        // display reactionTime
                        playerFirst = "Player 3 First " + String.valueOf(elapsed).concat(" ms");
                        // persist reactionDelay
//                        StatsModel stats = StatsModel.getStatsModel();
//                        stats.setFilename(player3);
                    }
                } else if (!second && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player3Score = elapsed;
                    second = true;
                    playerSecond = "Player 3 Second " + String.valueOf(elapsed).concat(" ms");
                } else if (!third && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player3Score = elapsed;
                    third = true;
                    playerThird = "Player 3 Third " + String.valueOf(elapsed).concat(" ms");
                } else if (!fourth && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player3Score = elapsed;
                    fourth = true;
                    playerFourth = "Player 3 Fourth " + String.valueOf(elapsed).concat(" ms");
                } else {
                    dummy = 3;
                }
            }
        } else{
            if(gameState == Statetype.READY){
                sounderror();
                playerFirst = "Player 3 Too Early";
               Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
            }
            gameState = Statetype.STOP;
            startOver(myView);
        }
    }
    public void player4(View view) {
        Long elapsed;
        View myView = view;
        if (gameState == Statetype.GO) {
            if (!play4) {
                play4 = true;
                if (!first && !firstbad) {
                    first = true;
                    elapsed = activity.reactionTimer.getElapsed();
                    if (elapsed == null) {
                        // display error message
                        sounderror();
                        playerFirst = "Player 4 Too Early ";
                        Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
                        firstbad = true;
                        gameState = Statetype.STOP;
                    } else {
                        elapsed = elapsed - 200L; // Take out display rendering time
                        if(elapsed <= 100){
                            playerFirst = "Player 4 Too Early ";
                            elapsed = 0L;
                            sounderror();
                            return;
                        }

                        player4Score = elapsed;
                        // display reactionTime
                        playerFirst = "Player 4 First " + String.valueOf(elapsed).concat(" ms");
                        // persist reactionDelay
//                        StatsModel stats = StatsModel.getStatsModel();
//                        stats.setFilename(player4);
                    }
                } else if (!second && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player4Score = elapsed;
                    second = true;
                    playerSecond = "Player 4 Second " + String.valueOf(elapsed).concat(" ms");
                } else if (!third && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player4Score = elapsed;
                    third = true;
                    playerThird = "Player 4 Third " + String.valueOf(elapsed).concat(" ms");
                } else if (!fourth && !firstbad) {
                    elapsed = activity.reactionTimer.getElapsed();
                    elapsed = elapsed - 200L; // Take out display rendering time
                    player4Score = elapsed;
                    fourth = true;
                    playerFourth = "Player 4 Fourth " + String.valueOf(elapsed).concat(" ms");

                } else {
                    dummy = 4;
                }
            }
        } else{
            if(gameState == Statetype.READY){
                sounderror();
                playerFirst = "Player 4 Too Early";
                Toast.makeText(activity, playerFirst, Toast.LENGTH_LONG).show();
            }
            gameState = Statetype.STOP;
            startOver(myView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

