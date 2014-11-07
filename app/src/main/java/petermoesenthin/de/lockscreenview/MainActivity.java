package petermoesenthin.de.lockscreenview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    public static final String DEBUG_TAG = "MainActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }

    public void startTimer(View view) {
        Log.i(DEBUG_TAG, "Starting timer");
        Toast.makeText(this,"Timer started!",Toast.LENGTH_SHORT).show();
        CountDownTimer cdt = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Log.i(DEBUG_TAG, "Starting service");
                Intent i = new Intent(mContext, OverlayService.class);
                mContext.startService(i);
            }

        }.start();
    }

}
