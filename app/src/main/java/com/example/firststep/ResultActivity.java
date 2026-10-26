package com.example.firststep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_result);
        int i = getIntent().getIntExtra("mission", 1);
        if (!MissionData.valid(i)) i=1;
        boolean demo = getIntent().getBooleanExtra("demo", true);
        ((TextView)findViewById(R.id.resultTitle)).setText(demo ? "화면 시연 완료" : "기억 조각을 발견했어요!");
        ((TextView)findViewById(R.id.resultInfo)).setText(demo ?
            "이 화면은 시연용입니다. 실제 센서 성공이나 방문 완료로 저장되지 않았습니다." :
            MissionData.TITLES[i] + "\n\n" + MissionData.LEARNING[i] + "\n\n진행 기록이 저장되었습니다.");
        findViewById(R.id.buttonCollection).setOnClickListener(v -> startActivity(new Intent(this, CollectionActivity.class)));
        findViewById(R.id.buttonHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
