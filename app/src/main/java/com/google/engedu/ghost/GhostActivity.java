/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        try {
            dictionary = new FastDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView label = findViewById(R.id.gameStatus);
        Log.i("1", label.getText().toString());
        onStart(null);

        final Button reset = findViewById(R.id.Reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onStart(view);
            }
        });

        final Button challenge = findViewById(R.id.Challenge);
        challenge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                challenge(view);
            }
        });

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

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        //userTurn = random.nextBoolean();
        userTurn = true;
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        Log.i("2", label.getText().toString());
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            Log.i("#", label.getText().toString());
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
                Log.i("here","here");

                TextView label = (TextView) findViewById(R.id.gameStatus);
                Log.i("3", label.getText().toString());
                // Do computer turn stuff then make it the user's turn again
                userTurn = false;
                label.setText(COMPUTER_TURN);
                TextView fragment = findViewById(R.id.ghostText);


                String wordFragment = fragment.getText().toString();
                Log.i("fragment is",wordFragment);
                if (wordFragment.length() >= 4 && dictionary.isWord(wordFragment)) {
                    label.setText("Computer Won");
                    return;



                } else {
                    String longerWord = dictionary.getAnyWordStartingWith(wordFragment);
                    Log.i("longer word",longerWord + "");
                    if (longerWord == null) {
                        label.setText("You lose");
                        return;


                    } else {
                        fragment.append(String.valueOf(longerWord.charAt(fragment.length())));

                    }
                }
                userTurn = true;
                label.setText(USER_TURN);
                Log.i("4", label.getText().toString());
                Log.i("Also here","also here");


            }




    /**
     * Handler for user key presses.
     *
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        userTurn = true;
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            TextView text = (TextView) findViewById(R.id.ghostText);
            String wordFragment = text.getText().toString() + ((char) event.getUnicodeChar());
            text.setText(wordFragment);
            Log.i("Info", "word fragment is " + wordFragment);
            userTurn = false;
            computerTurn();
            return true;



        } else{
            return super.onKeyUp(keyCode, event);
        }

    }


    public void challenge(View view) {
        TextView text = (TextView) findViewById(R.id.ghostText);
        String wordFragment = text.getText().toString();
        if (wordFragment.length() >= 4 && dictionary.isWord(wordFragment)) {
            text.setText("User Won");

        } else if (!dictionary.isWord(wordFragment) && dictionary.getAnyWordStartingWith(wordFragment) != null) {
            text.setText("Computer Won");
        } else {
            text.setText("User Won");
        }
        return;


    }
}
