package com.example.firststep;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectionActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_collection);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }
    @Override protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.collectionProgress)).setText("수집한 기억 " + ProgressStore.count(this) + " / 5개");
        LinearLayout holder = findViewById(R.id.collectionContainer);
        holder.removeAllViews();
        for (int i=0; i<5; i++) {
            View item = getLayoutInflater().inflate(R.layout.item_mission, holder, false);
            boolean done = ProgressStore.isCompleted(this, i);
            ((TextView)item.findViewById(R.id.itemNumber)).setText(done ? "발견" : "잠김");
            ((TextView)item.findViewById(R.id.itemTitle)).setText(MissionData.TITLES[i]);
            ((TextView)item.findViewById(R.id.itemSubtitle)).setText(done ? MissionData.LEARNING[i] : MissionData.PLACES[i] + " · 미완료");
            ((TextView)item.findViewById(R.id.itemState)).setText(done ? "기억 조각 수집" : "미수집");
            holder.addView(item);
        }
    }
}
