package com.devnull.braillewatchface;

//import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BrailleWatchface extends WatchFaceActivity {

    private TextView mTime, mBTime;
    private Typeface BrailleTypeface;

    private final static IntentFilter INTENT_FILTER;
    static {
        INTENT_FILTER = new IntentFilter();
        INTENT_FILTER.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER.addAction(Intent.ACTION_TIME_CHANGED);
    }

    private final String TIME_FORMAT_DISPLAYED = "KK:mm";

    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            mTime.setText(new SimpleDateFormat(TIME_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime()));
        }
    };

    private BroadcastReceiver mBTimeInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            mBTime.setText(new SimpleDateFormat(TIME_FORMAT_DISPLAYED).format(Calendar.getInstance().getTime()));
        }
    };

    @Override
    public void onScreenDim() {
        mTime.setTextColor(Color.WHITE);
        mBTime.setTextColor(Color.WHITE);
    }

    @Override
    public void onScreenAwake() {
        mTime.setTextColor(Color.RED);
        mBTime.setTextColor(Color.RED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_watchface);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTime = (TextView) stub.findViewById(R.id.watch_time);
                mTime.setTextColor(Color.WHITE);
                BrailleTypeface = Typeface.createFromAsset(getAssets(), "fonts/BRAILLE.ttf");
                mBTime = (TextView) stub.findViewById(R.id.watch_btime);
                mBTime.setTextColor(Color.WHITE);
                mBTime.setTypeface(BrailleTypeface);
                mTimeInfoReceiver.onReceive(BrailleWatchface.this, null);
                registerReceiver(mTimeInfoReceiver, INTENT_FILTER);
                registerReceiver(mBTimeInfoReceiver, INTENT_FILTER);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mBTimeInfoReceiver);
    }
}
