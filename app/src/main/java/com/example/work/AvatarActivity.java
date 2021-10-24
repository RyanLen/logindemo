package com.example.work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity {
    ImageView iv_avatar;
    Button btn_ok;
    GridView gv_avatars;

    private List<Integer> avatars;
    private int selected = R.drawable.avatar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        initData();
        initView();
        addListener();
    }

    private void initData() {
        avatars = new ArrayList<>();
        avatars.add(R.drawable.avatar1);
        avatars.add(R.drawable.avatar2);
        avatars.add(R.drawable.avatar3);
        avatars.add(R.drawable.avatar4);
        avatars.add(R.drawable.avatar5);
        avatars.add(R.drawable.avatar6);
        avatars.add(R.drawable.avatar7);
        avatars.add(R.drawable.avatar8);
        avatars.add(R.drawable.avatar9);
        avatars.add(R.drawable.avatar10);
        avatars.add(R.drawable.avatar11);
        avatars.add(R.drawable.avatar12);
        avatars.add(R.drawable.avatar13);
        avatars.add(R.drawable.avatar14);
        avatars.add(R.drawable.avatar15);
        avatars.add(R.drawable.avatar16);
    }

    private void initView() {
        iv_avatar = findViewById(R.id.iv_avatar);
        btn_ok = findViewById(R.id.btn_ok);
        gv_avatars = findViewById(R.id.gv_avatars);

        AvatarAdapter avatarAdapter = new AvatarAdapter(this, avatars);
        gv_avatars.setAdapter(avatarAdapter);
    }

    private void addListener() {
        gv_avatars.setOnItemClickListener((adapterView, view, i, l) -> {
            iv_avatar.setImageResource(avatars.get(i));
            selected = i;
        });

        btn_ok.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(RegisterActivity.AVATAR,avatars.get(selected));
            setResult(RESULT_OK,intent);
            finish();
        });
    }
}