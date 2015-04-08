package me.abhinavk.codenamex;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    SharedPreferences sp;
    public final static String EXTRA_MSG = "me.abhinavk.codenamex.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText emailfield = (EditText)findViewById(R.id.emailbox);
        final EditText pwdfield = (EditText)findViewById(R.id.pwdbox);
        TextView loggedinuser = (TextView)findViewById(R.id.text_resume_game);
        Button loginbtn = (Button)findViewById(R.id.loginbtn);
        LinearLayout resarea = (LinearLayout)findViewById(R.id.layout2);

        View.OnClickListener loginbtnlsnr = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new loginAuthentication(MainActivity.this).execute(emailfield.getText().toString(),pwdfield.getText().toString());
            }
        };
        loginbtn.setOnClickListener(loginbtnlsnr);

        View.OnClickListener resareals = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Category.class);
                startActivity(intent);
            }
        };
        resarea.setOnClickListener(resareals);

        // Get loggedin user
        sp = getSharedPreferences("loginfo",MODE_PRIVATE);
        if(sp.contains("loggedin")) {
            if(sp.getString("loggedin","") == "yes") {
                loggedinuser.setText("Resume as " + sp.getString("fname","") + " " + sp.getString("lname",""));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class loginAuthentication extends AsyncTask<String, Void, JSONObject> {

        private Activity activity;
        @Override
        protected JSONObject doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://cnxcnx.byethost3.com/login.php");
            List<NameValuePair> postparams = new ArrayList<NameValuePair>();
            postparams.add(new BasicNameValuePair("login_email",params[0]));
            postparams.add(new BasicNameValuePair("login_password",params[1]));
            Log.d("CRED",params[0]+ " "+params[1]);
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
                    Log.d("JSON","No json");
                }
            } catch(IOException e) {
                Log.d("JSON","No io");
            }
            return jsonObject;
        }

        public loginAuthentication(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPostExecute(JSONObject data) {
            super.onPostExecute(data);
            String email = "unknown";
            if(data.equals(null)) {
                //
            } else {
                try {
                    if (data.length() > 0) {
                        email = data.getString("email");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("email", data.getString("email"));
                        editor.putString("loggedin", "yes");
                        editor.putString("fname", data.getString("fname"));
                        editor.putString("lname", data.getString("lname"));
                        editor.putString("id", data.getString("id"));
                        editor.commit();
                        Log.d("ID",sp.getString("id",""));
                    }
                } catch (JSONException e) {

                }
            }
            if(email == "unknown") {
                Toast.makeText(getApplicationContext(), "Login failed. Try again.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Logged in as " + email, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(activity, Category.class);
                intent.putExtra(EXTRA_MSG, data.toString());
                startActivity(intent);
            }

        }
    }
}
