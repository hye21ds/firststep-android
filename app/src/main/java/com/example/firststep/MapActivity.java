package com.example.firststep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_map);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        findViewById(R.id.buttonMissions).setOnClickListener(v -> startActivity(new Intent(this, MissionListActivity.class)));
        LinearLayout container = findViewById(R.id.mapPlaceList);
        for (int i=0; i<MissionData.PLACES.length; i++) {
            TextView item = new TextView(this);
            item.setText((i+1) + ". " + MissionData.PLACES[i] + "  ·  " + MissionData.INPUTS[i]);
            item.setTextSize(16);
            item.setTextColor(0xFF1F2A40);
            item.setPadding(0, 20, 0, 20);
            container.addView(item);
        }
    }
}
