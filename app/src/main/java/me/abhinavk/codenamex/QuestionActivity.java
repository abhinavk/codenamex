package me.abhinavk.codenamex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuestionActivity extends ActionBarActivity {

    String current_cat = null;
    String current_id = null;
    String current_fname = null;
    String current_lname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questiona);

        // Get current category
        Intent i = getIntent();
        Bundle catb = i.getExtras();
        if(catb!=null) {
            current_cat = catb.getString("me.abhinavk.codenamex.WHICHCAT");
        }

        // Get the current user details in the instance
        SharedPreferences sp = getSharedPreferences("loginfo",MODE_PRIVATE);
        if(sp.contains("loggedin")) {
            if(sp.getString("loggedin","") == "yes") {
                current_id = sp.getString("id","");
                current_fname = sp.getString("fname","");
                current_lname = sp.getString("lname","");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    private class GetQuestion extends AsyncTask<String, Void, JSONObject> {

        private Activity activity;
        @Override

        protected JSONObject doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://cnxcnx.byethost3.com/get_ques.php");
            List<NameValuePair> postparams = new ArrayList<NameValuePair>();
            postparams.add(new BasicNameValuePair("user_id",current_id));
            postparams.add(new BasicNameValuePair("user_cat",current_cat));
            InputStream istream = null;
            JSONObject jsonObject = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(postparams));
            } catch(UnsupportedEncodingException e) {

            }
            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                istream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                try {
                    jsonObject = new JSONObject(builder.toString());
                } catch (JSONException e) {
                    Log.d("JSON", "No json");
                }
            } catch(IOException e) {
                Log.d("JSON","No io");
            }
            return jsonObject;
        }

        public GetQuestion(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Getting question...", Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPostExecute(JSONObject data) {
            super.onPostExecute(data);

            Random random = new Random();
            int qid = random.nextInt(data.length());

            
        }
    }
}
