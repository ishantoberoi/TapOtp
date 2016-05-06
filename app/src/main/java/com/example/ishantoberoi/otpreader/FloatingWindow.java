package com.example.ishantoberoi.otpreader;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ishantoberoi on 23/02/16.
 */
public class FloatingWindow extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private LinearLayout linearLayout;
    private Button btnStopFloatingWindow;
    private Button btnCopyOTP;
    private Button btnShareOtp;
    private TextView txtSmsText;
    private TextView txtSmsReceivedFrom;
    private ClipboardManager clipboardManager;
    private String smsFrom;
    private String smsBody;
    private String otp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(! (Settings.canDrawOverlays(getBaseContext())) ){
                return -99;
            }
        }
        Bundle b = intent.getExtras();
        Log.i("SMS From: ",b.getString("from"));
        Log.i("SMS Body: ",b.getString("message"));
        Log.i("SMS OTP: ",b.getString("otp"));
        smsFrom = b.getString("from");
        smsBody = b.getString("message");
        otp = b.getString("otp");
        fillDataAndShow();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= 23) {
            if(! (Settings.canDrawOverlays(getBaseContext())) ){
                return;
            }
        }
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        final WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(displayMetrics.widthPixels - 20, displayMetrics.heightPixels/3, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.gravity = Gravity.CENTER;

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.sms_data_layout, null);
        btnStopFloatingWindow = (Button) linearLayout.findViewById(R.id.btnStopFloatingWindow);
        btnCopyOTP = (Button) linearLayout.findViewById(R.id.btnCopyOTP);
        btnShareOtp = (Button) linearLayout.findViewById(R.id.btnShareOTP);
        txtSmsText = (TextView) linearLayout.findViewById(R.id.txtSmsText);
        btnCopyOTP.setOnClickListener(this);
        btnStopFloatingWindow.setOnClickListener(this);
        btnShareOtp.setOnClickListener(this);
        txtSmsReceivedFrom = (TextView) linearLayout.findViewById(R.id.txtSmsReceivedFrom);
        linearLayout.setVisibility(View.GONE);
        windowManager.addView(linearLayout, windowParams);


        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            private WindowManager.LayoutParams updatedWindowParamters = windowParams;
            int x, y;
            float touchedX, touchedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = updatedWindowParamters.x;
                        y = updatedWindowParamters.y;
                        touchedX = event.getRawX();
                        touchedY = event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedWindowParamters.x = (int) (x + (event.getRawX() - touchedX));
                        updatedWindowParamters.y = (int) (y + (event.getRawY() - touchedY));
                        windowManager.updateViewLayout(linearLayout, updatedWindowParamters);
                        break;

                    default:
                        break;
                }

                return false;
            }
        };

        linearLayout.setOnTouchListener(onTouchListener);
        btnShareOtp.setOnTouchListener(onTouchListener);
        btnCopyOTP.setOnTouchListener(onTouchListener);
        btnStopFloatingWindow.setOnTouchListener(onTouchListener);
    }

    public void fillDataAndShow() {
        txtSmsText.setText(smsBody);
        txtSmsReceivedFrom.setText(smsFrom);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStopFloatingWindow:
                windowManager.removeView(linearLayout);
                stopSelf();
                break;
            case R.id.btnCopyOTP:
                if(smsBody != null) {
                    clipboardManager = (ClipboardManager) getBaseContext().getSystemService(CLIPBOARD_SERVICE);
                    ClipData myClipObject = ClipData.newPlainText("smsData", otp);
                    clipboardManager.setPrimaryClip(myClipObject);
                    Toast.makeText(getBaseContext(), "Copied to clipboard "+otp, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Sorry, can not find any otp", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnShareOTP:
                String shareOtpString = "[TapOtp]Shared Text is "+otp;
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareOtpString);
                shareIntent.setType("text/plain");
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(shareIntent);
                break;
        }
    }
}
