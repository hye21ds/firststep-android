package com.example.firststep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView textProgress;
    private ProgressBar progressBar;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        textProgress = findViewById(R.id.textProgress);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.buttonStart).setOnClickListener(v -> open(MissionListActivity.class));
        findViewById(R.id.buttonMap).setOnClickListener(v -> open(MapActivity.class));
        findViewById(R.id.buttonCollection).setOnClickListener(v -> open(CollectionActivity.class));
    }
    @Override protected void onResume() {
        super.onResume();
        int total = ProgressStore.count(this);
        textProgress.setText("기억 조각 " + total + " / 5개");
        progressBar.setProgress(total * 20);
    }
    private void open(Class<?> type) { startActivity(new Intent(this, type)); }
}
