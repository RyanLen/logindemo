package com.example.work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    TextView tv_name;
    EditText et_edit;
    Button btn_save,btn_clear,btn_toManage,btn_close;

    private int id = 0;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
        addListener();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getInt("id");
        username = extras.getString("username");
    }

    private void initView() {
        tv_name = findViewById(R.id.tv_name);
        et_edit = findViewById(R.id.et_edit);
        btn_save = findViewById(R.id.btn_save);
        btn_clear = findViewById(R.id.btn_clear);
        btn_toManage = findViewById(R.id.btn_toManage);
        btn_close = findViewById(R.id.btn_close);

        tv_name.setText("此文档由" + username + "编辑");

        // 获取文档内容
        et_edit.setText(load(String.valueOf(id)));
    }

    private void addListener() {
        btn_toManage.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        });

        btn_clear.setOnClickListener(view -> et_edit.setText(""));

        btn_close.setOnClickListener(view -> finish());

        btn_save.setOnClickListener(view -> {
            String content = et_edit.getText().toString();
            // 保存文档文本
            save(String.valueOf(id),content);
        });
    }

    public String load(String id) {
        FileInputStream fis = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try {
            fis = openFileInput(id + ".txt");
            reader = new BufferedReader(new InputStreamReader(fis));

            String str;
            while ((str = reader.readLine()) != null) {
                content.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null)
                    reader.close();
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return content.toString();
    }

    public void save(String id,String content) {
        FileOutputStream fos = null;
        BufferedWriter writer = null;

        try {
            fos = openFileOutput(id + ".txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(content);
            Toast.makeText(this,"保存成功",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}