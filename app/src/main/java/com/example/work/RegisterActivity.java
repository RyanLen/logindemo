package com.example.work;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    EditText et_username,et_password,et_birthday;
    RadioGroup rg_gender;
    RadioButton rb_man,rb_female;
    Spinner sp_cities;
    ImageView iv_avatar;
    Button btn_chooseAvatar,btn_chooseDate,btn_register,btn_toLogin,btn_update,btn_delete;

    static final int REGISTER_AVATAR = 100;
    static final String AVATAR = "avatar";
    static final String[] cities = {"广州","深圳","佛山","茂名","汕头","汕尾","湛江","江门"};

    private int id = -1;
    private int gender = 0;
    private String city = "";
    private int avatar = R.drawable.avatar1;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initData();
        initView();
        addListener();
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
    }

    @SuppressLint("Range")
    private void initView() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_birthday = findViewById(R.id.et_birthday);
        rg_gender = findViewById(R.id.rg_gender);
        rb_man = findViewById(R.id.rb_man);
        rb_female = findViewById(R.id.rb_female);
        sp_cities = findViewById(R.id.sp_cities);
        iv_avatar = findViewById(R.id.iv_avatar);
        btn_chooseAvatar = findViewById(R.id.btn_chooseAvatar);
        btn_chooseDate = findViewById(R.id.btn_chooseDate);
        btn_register = findViewById(R.id.btn_register);
        btn_toLogin = findViewById(R.id.btn_toLogin);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);

        sp_cities.setAdapter(new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,cities));

        // 判断页面 如果id>0则可判定为 编辑页面
        // 编辑页面
        if (id > 0) {
            btn_register.setVisibility(View.GONE);
            btn_toLogin.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.VISIBLE);

            // 获取用户所有信息 并展示在界面中
            Uri uri = Uri.parse("content://" + UserProvider.AUTHORITY + "/query/" + id);
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToNext();
            et_username.setText(cursor.getString(cursor.getColumnIndex(UserProvider.USER_USERNAME)));
            et_password.setText(cursor.getString(cursor.getColumnIndex(UserProvider.USER_PASSWORD)));
            et_birthday.setText(cursor.getString(cursor.getColumnIndex(UserProvider.USER_BIRTHDAY)));
            gender = cursor.getInt(cursor.getColumnIndex(UserProvider.USER_GENDER));
            rb_man.setChecked(gender == 1);
            rb_female.setChecked(gender == 0);
            city = cursor.getString(cursor.getColumnIndex(UserProvider.USER_CITY));
            for (int i = 0; i < cities.length; i++) {
                if(city.equals(cities[i])) {
                    sp_cities.setSelection(i,true);
                    break;
                }
            }
            avatar = cursor.getInt(cursor.getColumnIndex(UserProvider.USER_AVATAR));
            iv_avatar.setImageResource(avatar);
        }

    }

    private void addListener() {
        rg_gender.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.rb_man:
                    gender = 1;
                    break;
                case R.id.rb_female:
                    gender = 0;
                    break;
            }
        });
        sp_cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = cities[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_chooseAvatar.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, AvatarActivity.class),REGISTER_AVATAR);
        });
        btn_chooseDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(
                    this, (datePicker, i, i1, i2) -> et_birthday.setText(i + "-" + i1 + "-" + i2)
                    ,year,month,day
            ).show();
        });
        btn_toLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        // 注册
        btn_register.setOnClickListener(view -> {
            ContentValues values = generateValues();

            // add user
            Uri uri = Uri.parse("content://" + UserProvider.AUTHORITY + "/insert");
            uri = getContentResolver().insert(uri, values);
            Toast.makeText(this,uri+"",Toast.LENGTH_LONG).show();
        });

        // 个人编辑
        btn_update.setOnClickListener(view -> {
            ContentValues values = generateValues();

            Uri uri = Uri.parse("content://" + UserProvider.AUTHORITY + "/update/" + id);
            int i = getContentResolver().update(uri, values, null, null);
            Toast.makeText(this,i>0 ? "更新成功" : "更新失败",Toast.LENGTH_LONG).show();
        });
        btn_delete.setOnClickListener(view -> {
            // 删除文件
            deleteFile(id + ".txt");

            // 从数据库中删除用户
            Uri uri = Uri.parse("content://" + UserProvider.AUTHORITY + "/delete/" + id);
            int i = getContentResolver().delete(uri, null, null);
            if (i > 0) {
                Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,LoginActivity.class));
            } else {
                Toast.makeText(this,"删除失败",Toast.LENGTH_LONG).show();
            }
        });
    }

    private ContentValues generateValues() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserProvider.USER_USERNAME,username);
        contentValues.put(UserProvider.USER_PASSWORD,password);
        contentValues.put(UserProvider.USER_GENDER,gender);
        contentValues.put(UserProvider.USER_CITY,city);
        contentValues.put(UserProvider.USER_BIRTHDAY,et_birthday.getText().toString());
        contentValues.put(UserProvider.USER_AVATAR,avatar);

        return contentValues;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REGISTER_AVATAR:
                avatar= data.getIntExtra(AVATAR, R.drawable.avatar1);
                iv_avatar.setImageResource(avatar);
                break;
        }
    }
}