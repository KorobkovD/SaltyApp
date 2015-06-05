package com.example.SaltyApp;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Denis on 25.11.2014.
 */
public class MainActivity extends Activity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // вывод тоста с сообщением о загрузке приложения
        Toast toast = Toast.makeText(getApplicationContext(),
                "Приложение загружается. Подождите...", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast.getView();
        ImageView waitView = new ImageView(getApplicationContext());
        waitView.setImageResource(R.drawable.wait);
        toastContainer.addView(waitView, 0);
        toast.show();
        //загрузка модуля речи
        tts = new TextToSpeech(this, this);
        btnSpeak = (Button) findViewById(R.id.button);
        txtText = (EditText) findViewById(R.id.editText);
        // узнать размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        // установка максимального значения высоты поля ввода текста
        txtText.setMaxHeight(metricsB.heightPixels-300);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("ru");
            int result = tts.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                btnSpeak.setEnabled(true);
                speakOut();
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    private void speakOut() {
        String text = txtText.getText().toString();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}