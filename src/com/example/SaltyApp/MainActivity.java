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
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;

/**
 * Created by Denis on 25.11.2014.
 */
public class MainActivity extends Activity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;
    private RadioGroup radiogroup;

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
        radiogroup = (RadioGroup)findViewById(R.id.radioGroup);
        btnSpeak = (Button) findViewById(R.id.button);
        txtText = (EditText) findViewById(R.id.editText);
        // узнать размеры экрана
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        // установка максимального значения высоты поля ввода текста
        txtText.setMaxHeight(metricsB.heightPixels - 300);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                speakOut();
            }
        });
        //TODO: меню приложения с хелпом и эбаутом.
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
            Toast.makeText(getApplicationContext(),
                    "TTS Initialization Failed :(\nУстановите синтезатор речи Google и включите его в настройках:" +
                            "\nСинтез речи -> Синтезатор речи Google",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Функция воспроизведения текста
     */
    private void speakOut() {
        int checkedRadioButtonId = radiogroup.getCheckedRadioButtonId();
        String text = "";
        switch (checkedRadioButtonId){
            case -1:
                Toast.makeText(getApplicationContext(), "Вы не выбрали речь!", Toast.LENGTH_LONG).show();
                break;
            case R.id.radioRUS:
                text = txtText.getText().toString();
                break;
            case R.id.radioSALT:
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Один момент...", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView waitView = new ImageView(getApplicationContext());
                waitView.setImageResource(R.drawable.wait);
                toastContainer.addView(waitView, 0);
                toast.show();
                text = saltyText(txtText.getText().toString());
                break;
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    /**
     * Строит по принятой строке "соленую" строку
     * @param s вводная строка
     * @return модифицированная введенная строка, на "соленый" манер
     */
    public String saltyText(String s) {
        HashSet<Character> vowels = new HashSet<Character>();
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        vowels.add('а');    vowels.add('и');    vowels.add('е');    vowels.add('ы');    vowels.add('ё');
        vowels.add('о');    vowels.add('э');    vowels.add('я');    vowels.add('ю');    vowels.add('у');
        for (int i = 0; i < builder.length(); i++) {
            if (vowels.contains(builder.charAt(i))) {
                builder.insert(i + 1, "-с" + builder.charAt(i));
                i += 3;
            }
        }
        Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_LONG).show();
        return builder.toString();
    }
}