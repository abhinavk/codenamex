package me.abhinavk.codenamex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class Category extends Activity {

    public final static String EXTRA_MSG = "me.abhinavk.codenamex.WHICHCAT";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /*
        Category maps
        0 - Mystery
        1 - Movies
        2 - Games
        3 - History
        4 - Science
        5 - Tech
        6 - Music
        7 - Personalities
        8 - Seasons
        */

        final ImageButton btn_0 = (ImageButton)findViewById(R.id.cat_mystery);
        final ImageButton btn_1 = (ImageButton)findViewById(R.id.cat_movies);
        final ImageButton btn_2 = (ImageButton)findViewById(R.id.cat_games);
        final ImageButton btn_3 = (ImageButton)findViewById(R.id.cat_history);
        final ImageButton btn_4 = (ImageButton)findViewById(R.id.cat_science);
        final ImageButton btn_5 = (ImageButton)findViewById(R.id.cat_tech);
        final ImageButton btn_6 = (ImageButton)findViewById(R.id.cat_music);
        final ImageButton btn_7 = (ImageButton)findViewById(R.id.cat_personalities);
        final ImageButton btn_8 = (ImageButton)findViewById(R.id.cat_seasons);

        final TextView username = (TextView)findViewById(R.id.text_resume_game);
        final TextView scoreview = (TextView)findViewById(R.id.text_score_game);

        SharedPreferences sp = getSharedPreferences("loginfo",MODE_PRIVATE);
        username.setText(sp.getString("fname","")+" "+sp.getString("lname",""));
        scoreview.setText(sp.getString("score",""));

        View.OnClickListener catbtnlistener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
                if(view == btn_2) {
                    intent.putExtra(EXTRA_MSG, "2");
                } else if (view == btn_1) {
                    intent.putExtra(EXTRA_MSG, "1");
                } else if (view == btn_0) {
                    intent.putExtra(EXTRA_MSG, "0");
                } else if (view == btn_8) {
                    intent.putExtra(EXTRA_MSG, "8");
                } else if (view == btn_3) {
                    intent.putExtra(EXTRA_MSG, "3");
                } else if (view == btn_4) {
                    intent.putExtra(EXTRA_MSG, "4");
                } else if (view == btn_5) {
                    intent.putExtra(EXTRA_MSG, "5");
                } else if (view == btn_6) {
                    intent.putExtra(EXTRA_MSG, "6");
                } else if (view == btn_7) {
                    intent.putExtra(EXTRA_MSG, "7");
                } else {
                    intent.putExtra(EXTRA_MSG, "none");
                }
                startActivity(intent);
            }
        };
        btn_0.setOnClickListener(catbtnlistener);
        btn_1.setOnClickListener(catbtnlistener);
        btn_2.setOnClickListener(catbtnlistener);
        btn_3.setOnClickListener(catbtnlistener);
        btn_4.setOnClickListener(catbtnlistener);
        btn_5.setOnClickListener(catbtnlistener);
        btn_6.setOnClickListener(catbtnlistener);
        btn_7.setOnClickListener(catbtnlistener);
        btn_8.setOnClickListener(catbtnlistener);

    }

}
