package com.example.ishantoberoi.otpreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ishantoberoi on 23/02/16.
 */
public class IncomingSMSReceiver extends BroadcastReceiver {

    // Get the object od SmsManager
    final SmsManager smsManager = SmsManager.getDefault();
    final String FROM = "from";
    final String MESSAGE = "message";
    final String OTP = "otp";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Receives a map of extended data from the intent
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for( int i = 0; i<pdusObj.length; i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String msgBody = currentMessage.getDisplayMessageBody();
                    Log.i("SMSReceiver", "SenderN0: " + phoneNumber + " Body: " + msgBody);

                    // Matching for specific words in sms to trigger Floating window
                    String patternString = "(OTP)|(KEY)|(PIN)|(PASSCODE)|(CODE)";
                    Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(msgBody);
                    if(matcher.find()) {
                        if(msgBody.matches("(?i:.*is\\s\\d{4,8}.*)")) {
                            patternString = "is\\s(\\d{4,8})";
                        } else if(msgBody.matches("(?i:.*\\d{4,8}\\sis.*)")) {
                            patternString = "(\\d{4,8})\\sis";
                        } else {
                            patternString = "is\\s(\\d{4,8})|\\s(\\d{4,8})\\s|\\s(\\d{4,8})\\s|^(\\d{4,8})\\s|\\s(\\d{4,8})\\.|-(\\d{4,8})";
                        }
                        pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                        matcher = pattern.matcher(msgBody);
                        if(matcher.find()) {
                            Intent startFloatingActivityIntent = new Intent(context, FloatingWindow.class);
                            Bundle smsContent = new Bundle();
                            smsContent.putString(FROM, phoneNumber);
                            smsContent.putString(MESSAGE, msgBody);
                            String otpExtracted = (matcher.group(0)).replaceAll("[A-Za-z\\s\\-\\.]", "");
                            Log.i("OTP: ", otpExtracted);
                            smsContent.putString(OTP, otpExtracted);
                            startFloatingActivityIntent.putExtras(smsContent);
                            context.startService(startFloatingActivityIntent);
                        } else {
                            Log.i("KeyFound", "No number in 4-8 digits found");
                            return;
                        }
                    } else {
                        Log.i("NoKeyFound", "No matching key found");
                        return;
                    }







                    if(matcher.find()) {
                        if(msgBody.matches(".*is\\s\\d{4,8}.*")) {
                            patternString = ".*is\\s(\\d{4,8}).*";
                            pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                        } else if(msgBody.matches(".*\\d{4,8}\\sis.*")) {
                            patternString = ".*(\\d{4,8})\\sis.*";
                            pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                        } else {
                            patternString = "is\\s(\\d{4,8})|\\s(\\d{4,8})\\s|\\s(\\d{4,8})\\s|^(\\d{4,8})\\s|\\s(\\d{4,8})\\.|-(\\d{4,8})";
                            pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                        }
                        matcher = pattern.matcher(msgBody);
                        if(matcher.find()) {
                            System.out.println("output:"+matcher.group(0));
                            Log.i("OTP: ", matcher.group(0));
                            Intent startFloatingActivityIntent = new Intent(context, FloatingWindow.class);
                            Bundle smsContent = new Bundle();
                            smsContent.putString(FROM, phoneNumber);
                            smsContent.putString(MESSAGE, msgBody);
                            String otpExtracted = (matcher.group(0)).trim().replaceAll("[.-a-z\\s]","");
                            smsContent.putString(OTP, otpExtracted);
                            startFloatingActivityIntent.putExtras(smsContent);
                            context.startService(startFloatingActivityIntent);
                        } else {
                            Log.i("KeyFound", "No number in 4-8 digits found");
                            return;
                        }
                    } else {
                        Log.i("NoKeyFound", "No matching key found");
                        return;
                    }
            }
        }

    } catch (Exception e) {
            Log.i("Exception","Exception Occured "+e.getMessage());
        }
    }
}
