package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private List<Word> wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWordDefinitions();

        Button searchButton = findViewById(R.id.buttonSearch);
        EditText wordEditText = findViewById(R.id.editTextWord);
        TextView definitionTextView = findViewById(R.id.textViewDefinition);

        searchButton.setOnClickListener(view -> {
            String searchWord = wordEditText.getText().toString().trim();
            String definition = searchWordDefinition(searchWord);

            if (definition != null) {
                definitionTextView.setText(definition);
            } else {
                definitionTextView.setText(getString(R.string.definition_not_found));
            }
        });

    }

    private void loadWordDefinitions() {
        try {
            InputStream inputStream = getAssets().open("dictionary.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            wordList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String word = jsonObject.getString("word");
                String definition = jsonObject.getString("definition");

                Word wordObj = new Word();
                wordObj.setWord(word);
                wordObj.setDefinition(definition);

                wordList.add(wordObj);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String searchWordDefinition(String searchWord) {
        for (Word word : wordList) {
            if (word.getWord().equalsIgnoreCase(searchWord)) {
                return word.getDefinition();
            }
        }
        return null;
    }
}