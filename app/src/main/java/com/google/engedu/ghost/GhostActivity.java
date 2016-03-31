package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView ghostText;
    private TextView gameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        try {
            InputStream thing = getAssets().open("words.txt");
            dictionary = new SimpleDictionary(thing);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ghostText = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("buggy", "key pressed");
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            String letter = String.valueOf((char)event.getUnicodeChar());
            ghostText.append( letter.toLowerCase() );
            if(checkWord(ghostText.getText().toString())){
                gameStatus.setText("Complete Word");
            }
        }
        else {
            return super.onKeyUp(keyCode, event);
        }
        Log.d("buggy", "Player turn over");
        userTurn = !userTurn;
        gameStatus.setText(COMPUTER_TURN);
        computerTurn();
        return true;
    }

    public boolean checkWord(String currentWord){
        return dictionary.isWord(currentWord);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
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

    public void challenge(View view){
        String currentWord = ghostText.getText().toString();
        if(currentWord.length() >=4 && checkWord(currentWord)){
            gameStatus.setText("THATS A WORD! PLAYER WINS!");
            return;
        }
        else{
            gameStatus.setText("THATS NOT A WORD/TOO SHORT! PLAYER LOSES");
        }
    }

    private void computerTurn() {
        Log.d("buggy", "computer turn start");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String currentWord = ghostText.getText().toString();
        Log.d("buggy", currentWord);
        if(currentWord.length() >=4 && checkWord(currentWord)){
            label.setText("THATS A WORD! COMPUTER WINS!");
            return;
        }
        String nextWord = dictionary.getAnyWordStartingWith(currentWord);
        Log.d("buggy", ""+nextWord);
        if(null == nextWord){
            label.setText("THERE ARE NO WORDS! COMPUTER WINS!");
            return;
        }
        String nextLetter = nextWord.substring(currentWord.length(), currentWord.length()+1);
        Log.d("buggy", nextLetter);
        ghostText.append( nextLetter );

        Log.d("buggy", "computer turn end");
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
