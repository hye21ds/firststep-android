package com.example.firststep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MissionListActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_mission_list);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }
    @Override protected void onResume() {
        super.onResume();
        LinearLayout container = findViewById(R.id.missionContainer);
        container.removeAllViews();
        for (int i = 0; i < MissionData.TITLES.length; i++) {
            final int index = i;
            View card = getLayoutInflater().inflate(R.layout.item_mission, container, false);
            ((TextView)card.findViewById(R.id.itemNumber)).setText(String.format(java.util.Locale.KOREA, "M%02d", i + 1));
            ((TextView)card.findViewById(R.id.itemTitle)).setText(MissionData.TITLES[i]);
            ((TextView)card.findViewById(R.id.itemSubtitle)).setText(MissionData.PLACES[i] + " · " + MissionData.INPUTS[i]);
            ((TextView)card.findViewById(R.id.itemState)).setText(ProgressStore.isCompleted(this, i) ? "실제 완료" : "탐색하기");
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, MissionDetailActivity.class);
                intent.putExtra("mission", index);
                startActivity(intent);
            });
            container.addView(card);
        }
    }
}
