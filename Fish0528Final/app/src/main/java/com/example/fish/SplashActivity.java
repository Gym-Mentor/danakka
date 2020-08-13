package com.example.fish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;


public class SplashActivity extends Activity implements AutoPermissionsListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash); //splash.xml 참조

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class); //인텐트 참조
                startActivity(intent); //액티비티 시작
                finish(); //액티비티 종료
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101); //권한 부여
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
    }
}