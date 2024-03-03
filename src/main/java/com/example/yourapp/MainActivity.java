package com.example.yourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName,editTextPhone,editTextmail;
    private Button btnSpeechInput;
    private Button btnRegister;

    private SpeechRecognizer speechRecognizer;

    private EditText currentEditText;
    private List<EditText> editTextList;


    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone=findViewById(R.id.editTextPhone);
        editTextmail=findViewById(R.id.editTextmail);

        editTextList = new ArrayList<>();
        editTextList.add(editTextName);
        editTextList.add(editTextPhone);
        editTextList.add(editTextmail);

        currentEditText = editTextList.get(0); // Set to the first EditText


        btnSpeechInput = findViewById(R.id.btnSpeechInput);
        btnRegister = findViewById(R.id.btnRegister);


        btnSpeechInput.setOnClickListener(v -> promptSpeechInput());
        btnRegister.setOnClickListener(v -> registerUser());

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new CustomRecognitionListener());
    }

    /*private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your name");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Speech recognition not supported on your device", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        Toast.makeText(this, "User Registered: " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (!result.isEmpty()) {
                    String spokenText = result.get(0);
                    currentEditText.setText(spokenText);

                    int currentIndex = editTextList.indexOf(currentEditText);
                    if (currentIndex < editTextList.size() - 1) {
                        // Move to the next EditText
                        currentEditText = editTextList.get(currentIndex + 1);
                        promptSpeechInput(); // Start speech input for the next EditText
                    } else {
                        // All EditText fields are filled. Handle completion if needed.
                        // For example, you can call a method to process the filled data.
                        registerUser();
                    }
                } else {
                    // Handle the case where there are no speech input results
                    Toast.makeText(getApplicationContext(), "No speech input detected. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Speech recognition not supported on your device", Toast.LENGTH_SHORT).show();
        }
    }


    private class CustomRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {}

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {}

        @Override
        public void onError(int error) {
            Toast.makeText(getApplicationContext(), "Speech recognition error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {}

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    }
}
