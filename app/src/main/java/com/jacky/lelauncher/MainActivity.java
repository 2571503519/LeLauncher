package com.jacky.lelauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    private Button btn2;
    private ArcSeekBar arcSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        arcSeekBar = findViewById(R.id.arcSeekBar);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        arcSeekBar.setShowTick(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                arcSeekBar.showAnimation(arcSeekBar.getProgress() == 100 ? 0 :arcSeekBar.getProgress(),100,3000);
                break;
            case R.id.btn2:
                arcSeekBar.showAnimation(arcSeekBar.getProgress() == 0 ? 0 :arcSeekBar.getProgress(),0,3000);
                break;
        }
    }
}