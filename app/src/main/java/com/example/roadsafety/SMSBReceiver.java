package com.example.roadsafety;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.ContactsContract;
        import android.telephony.SmsMessage;
        import android.widget.Toast;

        import static android.R.string.no;


public class SMSBReceiver extends BroadcastReceiver
{
     @Override
     public void onReceive(Context context, Intent intent){
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsm = null;
            String sms_str ="";
     if (bundle != null)
     {
         Object[] pdus = (Object[]) bundle.get("pdus");
         smsm = new SmsMessage[pdus.length];
         for (int i=0; i<smsm.length; i++){
             smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
             sms_str += "From: " + smsm[i].getOriginatingAddress();
             sms_str += "\r\n";
             sms_str += smsm[i].getMessageBody().toString();
             sms_str+= "\r\n";

         }



         Intent smsIntent=new Intent(context,MainActivity.class);
         smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         smsIntent.putExtra("sms_str", sms_str);
         context.startActivity(smsIntent);
 }
}
}