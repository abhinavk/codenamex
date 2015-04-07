package me.abhinavk.codenamex;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Category extends ActionBarActivity{

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

        final Button btn_0 = (Button)findViewById(R.id.cat_mystery);
        final Button btn_1 = (Button)findViewById(R.id.cat_movies);
        final Button btn_2 = (Button)findViewById(R.id.cat_games);
        final Button btn_3 = (Button)findViewById(R.id.cat_history);
        final Button btn_4 = (Button)findViewById(R.id.cat_science);
        final Button btn_5 = (Button)findViewById(R.id.cat_tech);
        final Button btn_6 = (Button)findViewById(R.id.cat_music);
        final Button btn_7 = (Button)findViewById(R.id.cat_personalities);
        final Button btn_8 = (Button)findViewById(R.id.cat_seasons);

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
