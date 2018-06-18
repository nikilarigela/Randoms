package com.randomsvtwo.nikil.randoms;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public TextView mTextMessage, txt1, txt2;
    private int id = 1, change = 1, index = 0, indexForPlayers = 0;
    private int high, low, newHigh, newLow;
    private EditText startText, endText, player;
    private CheckBox repeatCheck;
    private String randomString;
    public FrameLayout touch;
    private int randomNumber, highestIndex;
    private List<Integer> solution = new ArrayList<>();
    private int result;
    private Toast t;
    private Dialog openDialog, listDialog;
    private ImageView toss, addButton, listButton, clearButton,replayButton;
    private List<String> players = new ArrayList<>();
    private FlipAnimation flipAnimation;
    private ListView playerList;
    ArrayAdapter<String> adapter;

    // setting the bottom navugation buttons

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    id = 1;
                    ids();
                    return true;
                case R.id.navigation_dashboard:
                    id = 2;
                    ids();
                    return true;
                case R.id.navigation_notifications:
                    id = 3;
                    ids();
                    return true;
                case R.id.navigation_alphabets:
                    id = 4;
                    ids();
                    return true;
                case R.id.navigation_players:
                    id = 5;
                    ids();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Random Numbers");


        mTextMessage = (TextView) findViewById(R.id.message);
        txt1 = (TextView) findViewById(R.id.txt_1);
        txt2 = (TextView) findViewById(R.id.txt_2);
        startText = (EditText) findViewById(R.id.start_text);
        endText = (EditText) findViewById(R.id.end_text);
        repeatCheck = (CheckBox) findViewById(R.id.repeat_check);
        addButton = (ImageView) findViewById(R.id.addplayer);
        replayButton = (ImageView)findViewById(R.id.replay);
        listButton = (ImageView) findViewById(R.id.show_list);
        toss = (ImageView) findViewById(R.id.imageToss);
        toss.setVisibility(View.INVISIBLE);
        listButton.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        touch = (FrameLayout) findViewById(R.id.content);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        startText.addTextChangedListener(new MyTextWatcher(startText));
        endText.addTextChangedListener(new MyTextWatcher(endText));


        // validating the empty text
        // checking condition and passing the methods

        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 1) {

                    if (!validate(startText)) {
                        return;
                    }
                    if (!validate(endText)) {
                        return;
                    }
                    if (Integer.parseInt(startText.getText().toString()) == Integer.parseInt(endText.getText().toString())) {
                        mTextMessage.setText((startText.getText().toString()));
                        return;
                    }


                    if (Integer.parseInt(startText.getText().toString()) > Integer.parseInt(endText.getText().toString())) {
                        endText.setError("End is lower than high");
                        t.makeText(getApplicationContext(), "End is lower than high", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (repeatCheck.isChecked()) {
                        low = Integer.parseInt(startText.getText().toString());
                        high = Integer.parseInt(endText.getText().toString());
                        mTextMessage.setText(Integer.toString(randomNumbers(high, low)));


//                    mTextMessage.setText("low"+low+"high"+high);

                    } else {
                        if (change == 1) {
                            newLow = Integer.parseInt(startText.getText().toString());
                            newHigh = Integer.parseInt(endText.getText().toString());
                            highestIndex = newHigh - newLow;
                            addelements(newLow, newHigh);
                            change = 0;

                        } else {
                            mTextMessage.setText(String.valueOf(nonrepeat()));
                        }
                    }

                } else if (id == 2) {

                    randomColors();

                } else if (id == 3) {
                    flipAnimation = new FlipAnimation(toss, toss);
                    randomToss();
                } else if (id == 4) {
                    randomAlphabets();
                } else {
                    randomPlayers();

                }
            }
        });

        //checking if the checkbox is changed or not
        //if changed then index = 0
        repeatCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (repeatCheck.isChecked()) {


                } else {
                    index = 0;
                }
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players.clear();
                mTextMessage.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);
                listButton.setVisibility(View.VISIBLE);
                replayButton.setVisibility(View.INVISIBLE);
                indexForPlayers = 0;


            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog = new Dialog(MainActivity.this);
                openDialog.setContentView(R.layout.add_layout);
                openDialog.show();
                Button addBut = (Button) openDialog.findViewById(R.id.addButton);
                player = (EditText) openDialog.findViewById(R.id.playerText);
                addBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String playerString = player.getText().toString();
                        if (playerString.startsWith(" ") || playerString.equals("")) {
                            t.makeText(getApplicationContext(), "Empty Text or starts with space bar", Toast.LENGTH_SHORT).show();
                        } else {
                            addplayer(playerString);
                            player.setText("");
                        }
                    }
                });


            }
        });


        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (players.isEmpty()) {

                    t.makeText(getApplicationContext(), "No players are Added", Toast.LENGTH_SHORT).show();


                } else {
                    listDialog = new Dialog(MainActivity.this);
                    listDialog.setContentView(R.layout.list_layout);
                    playerList = (ListView) listDialog.findViewById(R.id.list);
                    clearButton = (ImageButton) listDialog.findViewById(R.id.clear_Button);
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, players);
                    playerList.setAdapter(adapter);
                    listDialog.show();
                    playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            players.remove(position);
                            listDialog.dismiss();
                            listDialog.show();
                        }
                    });
                    clearButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listDialog.dismiss();
                            players.clear();
                        }
                    });


                }
                listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Collections.shuffle(players);
                    }
                });

            }
        });

    }

    // adding the values to the elements

    private void addelements(int low, int high) {

        solution.clear();
        index = 0;
        for (int i = low; i <= high; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);
    }

    public void addplayer(String playerName) {
        checkDuplicate(playerName);
        if (checkDuplicate(playerName) == 0) {

        } else {
            players.add(playerName);

        }
        Collections.shuffle(players);
    }


    public int checkDuplicate(String playerNameDuplicate) {
        if (players.isEmpty()) {
            players.add(playerNameDuplicate);
            return 1;
        } else {
            for (int k = 0; k < players.size(); k++) {
                if (players.get(k).equals(playerNameDuplicate)) {
                    return 0;
                }
            }
        }

        return 1;
    }

    // shuffling the elements for non repeating values.
    private int nonrepeat() {


        if (index == highestIndex + 1) {
            t.makeText(getApplicationContext(), "Iteration Completed", Toast.LENGTH_SHORT).show();
            index = 0;
            Collections.shuffle(solution);
        } else {
            randomNumber = solution.get(index);
            System.out.println("index" + index);
            System.out.println("value" + randomNumber);
            index++;

        }
        return randomNumber;
    }

    //printing random numbers
    public int randomNumbers(int high, int low) {
        Random r = new Random();
        int i1 = r.nextInt((high - low) + 1) + low;
        return i1;
    }

    //changing random colors
    public void randomColors() {
        mTextMessage.setVisibility(View.INVISIBLE);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        touch.setBackgroundColor(color);

    }

    // randomly toss
    public void randomToss() {

        toss.startAnimation(flipAnimation);
        Random randomNum = new Random();
        result = randomNum.nextInt(2);
        if (result == 0) {

            mTextMessage.setText("Heads");
            mTextMessage.setTextColor(Color.parseColor("#ffffff"));
        } else {

            mTextMessage.setText("Tails");
            mTextMessage.setTextColor(Color.parseColor("#ffffff"));
        }

    }

    //printing random alphabets
    public void randomAlphabets() {
        Random rnd = new Random();
        String c = String.valueOf((char) (rnd.nextInt(26) + 'a'));
        mTextMessage.setText(c.toUpperCase());
    }


    public void randomPlayers() {
        if (players.isEmpty()) {
            t.makeText(getApplicationContext(), "ADD Players", Toast.LENGTH_SHORT).show();
        } else {
            replayButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            listButton.setVisibility(View.INVISIBLE);
            mTextMessage.setVisibility(View.VISIBLE);
            if (indexForPlayers >= players.size()) {
                indexForPlayers = 0;
                t.makeText(getApplicationContext(), "Players iteration Completed", Toast.LENGTH_SHORT).show();
                Collections.shuffle(players);
            } else {
                randomString = players.get(indexForPlayers);
                mTextMessage.setText(randomString.toUpperCase());
                System.out.println(indexForPlayers);
                indexForPlayers++;
            }
        }

    }


    //ids is for ready for background
    public void ids() {

        if (id == 1) {
            setTitle("Random Numbers");
            touch.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextMessage.setTextColor(Color.parseColor("#000000"));
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText("Touch to Generate");
            startText.setVisibility(View.VISIBLE);
            repeatCheck.setVisibility(View.VISIBLE);
            toss.setVisibility(View.INVISIBLE);
            endText.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            listButton.setVisibility(View.INVISIBLE);

        } else if (id == 2) {
            setTitle("Random Colors");
            touch.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextMessage.setTextColor(Color.parseColor("#000000"));
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText("Click it.Make it Color");
            startText.setVisibility(View.INVISIBLE);
            repeatCheck.setVisibility(View.INVISIBLE);
            endText.setVisibility(View.INVISIBLE);
            txt1.setVisibility(View.INVISIBLE);
            toss.setVisibility(View.INVISIBLE);
            txt2.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            listButton.setVisibility(View.INVISIBLE);

        } else if (id == 3) {
            setTitle("Random Toss");
            touch.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextMessage.setTextColor(Color.parseColor("#ffffff"));
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText("Flip it.");
            startText.setVisibility(View.INVISIBLE);
            endText.setVisibility(View.INVISIBLE);
            txt1.setVisibility(View.INVISIBLE);
            toss.setVisibility(View.VISIBLE);
            repeatCheck.setVisibility(View.INVISIBLE);
            txt2.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            listButton.setVisibility(View.INVISIBLE);


        } else if (id == 4) {
            setTitle("Random Alphabets");
            touch.setBackgroundColor(Color.parseColor("#ffffff"));
            mTextMessage.setTextColor(Color.parseColor("#000000"));
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText("A");
            startText.setVisibility(View.INVISIBLE);
            endText.setVisibility(View.INVISIBLE);
            txt1.setVisibility(View.INVISIBLE);
            toss.setVisibility(View.INVISIBLE);
            repeatCheck.setVisibility(View.INVISIBLE);
            txt2.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            listButton.setVisibility(View.INVISIBLE);


        } else {
            if (players.size() != 0) {
                setTitle("Random Players");
                touch.setBackgroundColor(Color.parseColor("#ffffff"));
                startText.setVisibility(View.INVISIBLE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(players.get(indexForPlayers));
                System.out.println(indexForPlayers);
                endText.setVisibility(View.INVISIBLE);
                txt1.setVisibility(View.INVISIBLE);
                toss.setVisibility(View.INVISIBLE);
                repeatCheck.setVisibility(View.INVISIBLE);
                txt2.setVisibility(View.INVISIBLE);
                replayButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                listButton.setVisibility(View.INVISIBLE);

            } else {
                setTitle("Random Players");
                touch.setBackgroundColor(Color.parseColor("#ffffff"));
                mTextMessage.setVisibility(View.INVISIBLE);
                startText.setVisibility(View.INVISIBLE);
                endText.setVisibility(View.INVISIBLE);
                txt1.setVisibility(View.INVISIBLE);
                toss.setVisibility(View.INVISIBLE);
                repeatCheck.setVisibility(View.INVISIBLE);
                txt2.setVisibility(View.INVISIBLE);
                addButton.setVisibility(View.VISIBLE);
                listButton.setVisibility(View.VISIBLE);
                replayButton.setVisibility(View.INVISIBLE);
            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent it = new Intent(MainActivity.this, About.class);
            startActivity(it);
        } else if (id == R.id.action_rate) {

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=5599685400679741226")));
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.start_text:
                    change = 1;
                    validate(startText);
                    break;
                case R.id.end_text:
                    change = 1;
                    validate(endText);
                    break;


            }
        }
    }


    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError("Input is Empty");
            editText.requestFocus();
            return false;
        } else {
            editText.setEnabled(true);
        }

        return true;
    }


}
