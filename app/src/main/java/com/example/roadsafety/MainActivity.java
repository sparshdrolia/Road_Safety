package com.example.roadsafety;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnInitListener{

    private int MY_DATA_CHECK_CODE = 0;

    private Button button;
    private EditText editText;
    private TextToSpeech myTTS;
    private TextView textView;
    private RadioButton converttts;

    private String smstr;

    Runnable parallel = new Runnable(){
        @Override
        public void run() {
            speak(smstr);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        converttts = (RadioButton) findViewById(R.id.converttts);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.READ_SMS"},1);

        Intent sms_intent = getIntent();
        Bundle b = sms_intent.getExtras();

        try {
            smstr = b.getString("sms_str");
            textView.setText(smstr);
        }
        catch (Exception e){

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(converttts.isChecked()) {
                    speak(editText.getText().toString());
                    textView.setText(editText.getText().toString());
                }
                else{
                    //speak(textView.getText().toString());
                    new Thread(parallel).start();
                    moveTaskToBack(true);
                    while(myTTS.isSpeaking()){}
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MY_DATA_CHECK_CODE){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                myTTS = new TextToSpeech(this, this);
            }
            else{
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    private void speak(String words){
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

                myTTS.speak("",TextToSpeech.QUEUE_ADD,null);
                myTTS.speak(textView.getText().toString(), TextToSpeech.QUEUE_ADD, null);
        }
        else{
            Toast.makeText(this,"Error : Cannot read SMS",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if(status == TextToSpeech.ERROR){
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }

    /*private String getMessage(){
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        String msgData;
        if (cursor.moveToFirst()) {
            do {
                msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }

            } while (cursor.moveToNext());
        } else {
            msgData = "";
        }
        return msgData;
    }*/




}
