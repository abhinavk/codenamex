package me.abhinavk.codenamex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


public class QuestionActivity extends ActionBarActivity {

    String current_cat = null;
    String current_id = null;
    String current_fname = null;
    String current_lname = null;
    String current_ques_id = null;
    String current_ques_str = null;
    String current_answ = null;
    String current_ques_points = null;
    String current_ques_type = null;

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

        final Button submit_btn = (Button)findViewById(R.id.submit);
        //final Button changeq_btn = (Button)findViewById(R.id.change_ques);
        final EditText user_answer = (EditText)findViewById(R.id.editText);

        // Get the current user details in the instance
        SharedPreferences sp = getSharedPreferences("loginfo",MODE_PRIVATE);
                current_id = sp.getString("id","");
                current_fname = sp.getString("fname","");
                current_lname = sp.getString("lname","");
        new GetQuestion(QuestionActivity.this).execute(current_id,current_cat);
        // Submit Button action
        View.OnClickListener submitbtnlsnr = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Pattern.matches(current_answ,user_answer.getText().toString())) {
                    Log.d("ANSWER",current_id+current_ques_id+current_ques_points);
                    new SendAnswer(QuestionActivity.this).execute(current_id, current_ques_id, current_ques_points);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong answer. Try again or go back to category page.", Toast.LENGTH_LONG).show();
                }
            }
        };
        submit_btn.setOnClickListener(submitbtnlsnr);
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
            HttpPost httpPost = new HttpPost("http://cnxcnx.byethost3.com/get_ques2.php");
            List<NameValuePair> postparams = new ArrayList<NameValuePair>();
            postparams.add(new BasicNameValuePair("user_id",params[0]));
            postparams.add(new BasicNameValuePair("cat",params[1]));
            Log.v("TAGG",params[0]+params[1]);
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
                //Log.d("RESP",response.toString());
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

            try {
                //Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_LONG).show();
                JSONArray curr_ques_array = data.getJSONArray("questions");
                Random random = new Random();
                int qid = random.nextInt(curr_ques_array.length());
                JSONObject current_ques_obj = curr_ques_array.getJSONObject(qid);
                current_ques_id = current_ques_obj.getString("id");
                current_ques_str = current_ques_obj.getString("ques");
                current_answ = current_ques_obj.getString("ans");
                current_ques_points = current_ques_obj.getString("points");
                current_ques_type = current_ques_obj.getString("qtype");
                Log.d("QUes",current_ques_str);
                final TextView qtext = (TextView)findViewById(R.id.textView);
                final ImageView qimg = (ImageView)findViewById(R.id.imageView);

                if(Integer.parseInt(current_ques_type) < 2) {
                    qtext.setVisibility(View.VISIBLE);
                    qimg.setVisibility(View.INVISIBLE);
                    qtext.setText(current_ques_str);
                } else {
                    qimg.setVisibility(View.VISIBLE);
                    qtext.setVisibility(View.INVISIBLE);
                    Log.d("QUES",current_ques_str);
                    new GetImage(QuestionActivity.this).execute(current_ques_str);
                }
            } catch (JSONException e) {

            }


        }
    }

    private class SendAnswer extends AsyncTask<String, Void, JSONObject> {

        private Activity activity;
        @Override

        protected JSONObject doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://cnxcnx.byethost3.com/update_score.php");
            List<NameValuePair> postparams = new ArrayList<NameValuePair>();
            postparams.add(new BasicNameValuePair("user_id",params[0]));
            postparams.add(new BasicNameValuePair("user_ques",params[1]));
            postparams.add(new BasicNameValuePair("user_points",params[2]));
            InputStream istream = null;
            Log.d("SEnd ans",params[0]+" "+params[1]+" "+params[2]);
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

        public SendAnswer(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Correct answer. Updating score...", Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPostExecute(JSONObject data) {
            super.onPostExecute(data);

            try {
                String reply = data.getString("score");
                Log.d("REPLY-newscore",reply);
                SharedPreferences sp = getSharedPreferences("loginfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("score",reply);
                editor.commit();
            } catch (JSONException e) {

            }
            activity.finish();
        }
    }

    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        private Activity activity;

        HttpGet httpRequest = null;
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL("http://cnxcnx.byethost3.com/" + params[0]);
                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                InputStream instream = bufHttpEntity.getContent();
                bitmap = BitmapFactory.decodeStream(instream);

                Log.d("QUESTION","params[0]");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
            }
            return bitmap;
        }

        public GetImage(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Bitmap data) {
            super.onPostExecute(data);

            final ImageView qimg = (ImageView)findViewById(R.id.imageView);
            qimg.setImageBitmap(data);
        }
    }

}
