/**
 * login controller
 * by jh
 */
package com.minewbeacon.blescan.demo;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1000;

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

        //블루투스, 위치권환 체크
        checkBluetooth();
        checkLocationPermition();

    }

    //위치 권한 체크
    private void checkLocationPermition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(permissionCheck == PackageManager.PERMISSION_DENIED){

                // 권한 없음
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);

            } else{

                // ACCESS_FINE_LOCATION 에 대한 권한이 이미 있음.

            }


        }

// OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
        else{

        }
    }

    // 블루투스 권한 체크
    /**
     * check Bluetooth state
     */
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }

    //
    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    //로그인 버튼
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

    //회원가입 버튼
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

