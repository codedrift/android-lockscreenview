package petermoesenthin.de.lockscreenview;/*
 * Copyright (C) 2014 Peter Mösenthin <peter.moesenthin@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;


public class OverlayService extends Service {

    public static final String DEBUG_TAG = "OverlayService";

    WindowManager mWindowManager;
    View mView;
    private Context mContext;
    Animation mAnimation;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(DEBUG_TAG, "onBind called");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(DEBUG_TAG, "onStartCommand called");
        registerOverlayReceiver();
        showView();
        return START_STICKY_COMPATIBILITY;
    }



    private void showView() {
        Log.i(DEBUG_TAG, "Attempting to show view");
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.lock_screen_view, null);

        final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON ,
                PixelFormat.RGBA_8888);


        mView.setVisibility(View.VISIBLE);
        //mAnimation = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_in);
        //mView.setAnimation(mAnimation);
        mWindowManager.addView(mView, mLayoutParams);
    }

    private void hideView(){
        Log.i(DEBUG_TAG, "Hiding view");
        if(mView != null && mWindowManager != null){
            mWindowManager.removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onDestroy() {
        unregisterOverlayReceiver();
        super.onDestroy();
    }

    private void registerOverlayReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(overlayReceiver, filter);
    }

    private void unregisterOverlayReceiver() {
        hideView();
        unregisterReceiver(overlayReceiver);
    }


    private BroadcastReceiver overlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(DEBUG_TAG, "[onReceive]" + action);
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                showView();
            }
            else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                hideView();
            }
            else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                hideView();
            }
        }
    };
}
