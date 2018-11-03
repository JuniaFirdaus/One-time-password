package juniafirdaus.com.otpactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class OtpReceiver extends BroadcastReceiver {

    public OtpReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] objects = (Object[]) bundle.get("pdus");
                for (int i = 0; i < objects.length; i++) {
                    SmsMessage smsMessage = getIncomingMessage(objects[i], bundle);
                    String phoneNumber = smsMessage.getDisplayOriginatingAddress();
                    String sendNumber = phoneNumber;
                    String message = smsMessage.getDisplayMessageBody();
                    Log.i("ISI_SMS", "senderNum: " + sendNumber + "; message: " + message);
                    Intent showIntent = new Intent(context, MainActivity.class);
                    showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showIntent.putExtra(MainActivity.EXTRA_SMS_NO, phoneNumber);
                    showIntent.putExtra(MainActivity.EXTRA_SMS_MESSAGE, message);
                    context.startActivity(showIntent);

                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSms = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSms = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSms;
    }
}
