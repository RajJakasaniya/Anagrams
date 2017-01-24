package com.google.engedu.anagrams;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class AnagramsActivity extends AppCompatActivity {
    public static final String START_MESSAGE = "Find as many words as possible that can be formed by adding one letter to <big>%s</big> (but that do not contain the substring %s).";
    private AnagramDictionary dictionary;
    public static final String MID_MESSAGE="Find as many words as possible that can be formed by adding one letter to <big>%s</big>   <big>%s</big> (but that do not contain the substring %s).";
    private String currentWord,cW;
    private String anotherWord;
    private ArrayList<String> anagrams;
    private ArrayList<String> anagramsForTwoWord;
    int index=0;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new AnagramDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        // Set up the EditText box to process the content of the box when the user hits 'enter'
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        tv=(TextView)findViewById(R.id.header);
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if(index==0 || index==1) {
                        processWord(editText);
                    }else if(index==2) {
                        processWordTwo(editText);
                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void processWord(EditText editText) {
        TextView resultView = (TextView) findViewById(R.id.resultView);
        String word = editText.getText().toString().trim().toLowerCase();
        if (word.length() == 0) {
            return;
        }
        String color = "#cc0029";
        if (dictionary.isGoodWord(word, currentWord) && anagrams.contains(word)) {
            anagrams.remove(word);
            color = "#00aa29";
        } else {
            word = "X " + word;
        }
        resultView.append(Html.fromHtml(String.format("<font color=%s>%s</font><BR>", color, word)));
        editText.setText("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
    }

    private void processWordTwo(EditText editText) {
        TextView resultView = (TextView) findViewById(R.id.resultView);
        String word = editText.getText().toString().trim().toLowerCase();
        String []array=word.split(" ");
        if (word.length() == 0) {
            return;
        }
        String color = "#cc0029";
        if (dictionary.isGoodWord(array[0], currentWord) && (dictionary.isGoodWord(array[1],anotherWord)) && anagramsForTwoWord.contains(word)) {
            anagrams.remove(word);
            color = "#00aa29";
        } else {
            word = "X " + word;
        }
        resultView.append(Html.fromHtml(String.format("<font color=%s>%s</font><BR>", color, word)));
        editText.setText("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anagrams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean defaultAction(View view) {
        TextView gameStatus = (TextView) findViewById(R.id.gameStatusView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView resultView = (TextView) findViewById(R.id.resultView);
        if (currentWord == null) {
            currentWord = dictionary.pickGoodStarterWord();
            if(index==0){
                index++;
                anagrams = dictionary.getAnagramsWithOneMoreLetter(currentWord);
            }else{
                index++;
                anagrams = dictionary.getAnagramsWithTwoMoreLetter(currentWord);
            }

            gameStatus.setText(Html.fromHtml(String.format(START_MESSAGE, currentWord.toUpperCase(), currentWord)));
            fab.setImageResource(android.R.drawable.ic_menu_help);
            fab.hide();
            resultView.setText("");
            editText.setText("");
            editText.setEnabled(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }else if(currentWord=="goForTwo") {
            String list[];
            list=dictionary.pickGoodStarterWordForTwoStrings();
            currentWord =  list[0];
            cW=currentWord;
            index=0;
            anotherWord = list[1];
            anagramsForTwoWord =dictionary.getAnagramsWithOneMoreLetterTwoStrings(currentWord,anotherWord);
            gameStatus.setText(Html.fromHtml(String.format(MID_MESSAGE, currentWord.toUpperCase(),anotherWord.toUpperCase(), currentWord,anotherWord)));
            fab.setImageResource(android.R.drawable.ic_menu_help);
            fab.hide();
            resultView.setText("");
            editText.setText("");
            editText.setEnabled(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            currentWord="bcd";
        }else if(currentWord=="bcd"){
            editText.setText(cW+" "+anotherWord);
            editText.setEnabled(false);
            fab.setImageResource(android.R.drawable.ic_media_play);
            currentWord=null;
            resultView.append(TextUtils.join("\n", anagramsForTwoWord));
            gameStatus.append(" Hit 'Play' to start again");
        }else{
            editText.setText(currentWord);
            editText.setEnabled(false);
            fab.setImageResource(android.R.drawable.ic_media_play);
            if(index==1 || index==0){
                currentWord=null;
            }else{
                currentWord = "goForTwo";
            }

            resultView.append(TextUtils.join("\n", anagrams));
            gameStatus.append(" Hit 'Play' to start again");
            if(index==0){
                tv.setText("For One Letters");
            }else if(index==1){
                tv.setText("For Two Letters");
            }else{
                tv.setText("For Two Words");
            }
        }
        return true;
    }

}
