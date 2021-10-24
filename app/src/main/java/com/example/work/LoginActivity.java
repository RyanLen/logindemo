package com.example.work;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText et_username,et_password;
    CheckBox cb_remember;
    Button btn_login,btn_clear,btn_toRegister;

    private static final String AUTHORITY = "com.ryan.work";
    private static final String SP_NAME = "user";
    private static final String SP_ID = "id";
    private static final String SP_USERNAME = "username";
    private static final String SP_PASSWORD = "password";

    private String spUsername = "";
    private String spPassword = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initData();
        addListener();
    }
    
    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        cb_remember = findViewById(R.id.cb_remember);
        btn_login = findViewById(R.id.btn_login);
        btn_clear = findViewById(R.id.btn_clear);
        btn_toRegister = findViewById(R.id.btn_toRegister);
    }

    private void initData() {
        // Get the userInfo through SP
        SharedPreferences sp = getSharedPreferences(SP_NAME,MODE_PRIVATE);
        spUsername = sp.getString(SP_USERNAME, "");
        spPassword = sp.getString(SP_PASSWORD, "");

        // initData
        et_username.setText(spUsername);
        et_password.setText(spPassword);
    }
    
    @SuppressLint("Range")
    private void addListener() {
        btn_clear.setOnClickListener(view -> {
            et_username.setText("");
            et_password.setText("");
            cb_remember.setChecked(false);
        });

        btn_toRegister.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        });

        btn_login.setOnClickListener(view -> {
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();

            // query user's password & validate
            Uri uri = Uri.parse("content://" + AUTHORITY + "/queryByName");
            Cursor cursor = getContentResolver()
                    .query(uri, null, "username=?", new String[]{ username }, null);
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndex(UserProvider.USER_ID));
            String str_password = cursor.getString(cursor.getColumnIndex(UserProvider.USER_PASSWORD));

            if (str_password.equals(password)) {
                Toast.makeText(this,"Login successfully",Toast.LENGTH_LONG).show();

                // remember userInfo
                if (cb_remember.isChecked()) {
                    SharedPreferences.Editor editor = getSharedPreferences(SP_NAME,MODE_PRIVATE).edit();
                    editor.putInt(SP_ID,id);
                    editor.putString(SP_USERNAME,username);
                    editor.putString(SP_PASSWORD,password);
                    editor.apply();
                }

                Intent intent = new Intent(this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                bundle.putString("username",username);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast.makeText(this,"Failed to login!",Toast.LENGTH_LONG).show();
            }

        });
    }
}