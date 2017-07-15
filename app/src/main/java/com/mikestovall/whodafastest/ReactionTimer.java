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
import android.os.Handler;

import java.util.Random;

/**
 * Created by mstovall on 17-01-20.
 */
public abstract classReactionTimer {
    private static Long MAX_DELAY = 5000L;
    private static Long MIN_DELAY = 500L;

    private Handler handler;
    private Long delay;
    private Long startTime;



    abstract void onStart();
    private Runnable onStartRunnable;
    private Runnable onStartRunnable1;
    private Runnable onStartRunnable2;
    abstract void onComplete();
    abstract void onKill();
    abstract void displayYellow();
    abstract void displayScores();
//    abstract void displayGreen();

    public ReactionTimer() {
        this.handler = new Handler();
    }

    public void onlydelay(long delay) {
        final ReactionTimer that2 = this;
        this.onStartRunnable2 = new Runnable() {
            @Override
            public void run() {
                that2.displayScores();
            }
        };
        handler.postDelayed(this.onStartRunnable2, delay);
    }


    public void startdelay(long delay) {
//        this.startTime = System.currentTimeMillis();
        final ReactionTimer that1 = this;
        this.onStartRunnable1 = new Runnable() {
            @Override
            public void run() {
                that1.displayYellow(); // Displays RED STOP
                start();
            }
        };
        handler.postDelayed(this.onStartRunnable1, delay);
        this.onStart();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        final ReactionTimer that = this;
        this.onStartRunnable = new Runnable() {
            @Override
            public void run() {

                that.onComplete();
                onlydelay(1000L);
            }
        };
        handler.postDelayed(this.onStartRunnable, this.randomDelay());
//        onStart();
//      long startTime = 3000L;
//        this.startdelay(startTime);
    }
   private Long randomDelay() {
        Random r = new Random();
        this.delay = (long)(r.nextInt((int)(MAX_DELAY - MIN_DELAY + 1)) + MIN_DELAY);
        return this.delay;
    }
    private Long getDelay() {
        return this.delay;
    }

    public Long getElapsed() {
        if (this.startTime == null) {
            return null; // uninitialized startTime
        }
        Long elapsed = System.currentTimeMillis() - this.startTime - this.getDelay();
        if (elapsed < 0) {
            return null;
        }
        return elapsed;
    }
    public void kill() {
        handler.removeCallbacks(this.onStartRunnable);
        handler.removeCallbacks(this.onStartRunnable1);
        handler.removeCallbacks(this.onStartRunnable2);
        this.onKill();
    }
    public void restart() {
        this.kill();
        this.start();
    }

    public void saveReactionTime(Long elapsed, Context cxt) {
        StatsModel.getStatsModel().saveReactionTime(elapsed, cxt);
    }
}
