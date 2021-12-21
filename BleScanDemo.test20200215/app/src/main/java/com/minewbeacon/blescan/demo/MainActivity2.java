package com.minewbeacon.blescan.demo;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;

import com.yuliwuli.blescan.demo.R;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText mEtEmail, mEtPwd;
    private Context mContext;
    private CheckBox mCheckbox;


    private MinewBeaconManager mMinewBeaconManager;
    MainActivity MA = (MainActivity) MainActivity.activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MA.finish();
        Toast.makeText(MainActivity2.this, "블루젠트 이동", Toast.LENGTH_SHORT).show();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("bluzent");

        mEtEmail = findViewById(R.id.Lg_email);
        mEtPwd = findViewById(R.id.Lg_pwd);
        mCheckbox = findViewById(R.id.autoLogin);
        mContext = this;

        // 자동로그인 확인
        boolean check = PreferenceManager.getBoolean(mContext, "checked");

        if (String.valueOf(check).equals("true")) {
            mCheckbox.setChecked(true);
            mEtEmail.setText(PreferenceManager.getString(mContext, "ID"));
            mEtPwd.setText(PreferenceManager.getString(mContext, "PW"));
        } else {
            mCheckbox.setChecked(false);
            mEtEmail.setText("");
            mEtPwd.setText("");
        }
    }

    public void myListener(View target) {
        String strEmail = mEtEmail.getText().toString();
        String strPwd = mEtPwd.getText().toString();

        try {
            mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 로그인 성공
                        Toast.makeText(MainActivity2.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                        String strEmail = mEtEmail.getText().toString();
                        String strPwd = mEtPwd.getText().toString();


                        CheckBox Autologin = findViewById(R.id.autoLogin);

                        boolean checked = Autologin.isChecked();

                        if (checked) {
                            Toast.makeText(getApplicationContext(), String.valueOf(checked), Toast.LENGTH_SHORT).show();
                            PreferenceManager.setString(mContext, "ID", strEmail);
                            PreferenceManager.setString(mContext, "PW", strPwd);
                            PreferenceManager.setBoolean(mContext, "checked", true);

                        } else {
                            Toast.makeText(getApplicationContext(), String.valueOf(checked), Toast.LENGTH_SHORT).show();
                            PreferenceManager.setString(mContext, "ID", "");
                            PreferenceManager.setString(mContext, "PW", "");
                            PreferenceManager.setBoolean(mContext, "checked", false);
                        }


                        Intent intent = new Intent(getApplicationContext(), attendance.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity2.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }


    }

    public void myListener2(View target) {

        String strEmail = mEtEmail.getText().toString();
        String strPwd = mEtPwd.getText().toString();


        CheckBox Autologin = findViewById(R.id.autoLogin);
        Autologin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();

                if (checked) {
                    Toast.makeText(getApplicationContext(), String.valueOf(checked), Toast.LENGTH_SHORT).show();
                    PreferenceManager.setString(mContext, "ID", strEmail);
                    PreferenceManager.setString(mContext, "PW", strPwd);
                    PreferenceManager.setBoolean(mContext, "checked", true);

                } else {
                    Toast.makeText(getApplicationContext(), String.valueOf(checked), Toast.LENGTH_SHORT).show();
                    PreferenceManager.setString(mContext, "ID", "");
                    PreferenceManager.setString(mContext, "PW", "");
                    PreferenceManager.setBoolean(mContext, "checked", false);
                }
            }

        });

    }

    public void myListener3(View target) {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
        finish();
    }

}

