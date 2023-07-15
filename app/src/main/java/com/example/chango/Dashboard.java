package com.example.chango;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class Dashboard extends AppCompatActivity {
    public static String PREFS_NAME= "MyprefsFile";
    BottomNavigationView bottom;
    EditText secretcodebox;
    Button joinbtn, sharebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        secretcodebox=findViewById(R.id.codeBox);
        joinbtn= findViewById(R.id.joinBtn);
        sharebtn= findViewById(R.id.shareBtn);
        bottom=findViewById(R.id.bottomNavigationView);
        URL serverURL;

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.logout){
                    SharedPreferences sharedPreferences = getSharedPreferences(Dashboard.PREFS_NAME, 0);
                    boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", true);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("hasLoggedIn", false);
                    editor.commit();
                    if(!hasLoggedIn) {
                        startActivity(new Intent(Dashboard.this, LoginActivity.class));
                        finish();
                    }
                }
                return false;
            }
        });

        try {
            serverURL= new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL).build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options= new JitsiMeetConferenceOptions.Builder()
                        .setRoom(secretcodebox.getText().toString())
                        .build();

                JitsiMeetActivity.launch(Dashboard.this, options);
            }
        });
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "code is " + secretcodebox.getText().toString());
                    startActivity(Intent.createChooser(intent, "Share Via"));
                }
        });
        }
    }