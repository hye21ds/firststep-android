package com.example.firststep;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Locale;

public class MissionDetailActivity extends Activity implements SensorEventListener {
    private int mission;
    private TextView status;
    private TextView statusDetail;
    private ProgressBar meter;
    private SensorManager manager;
    private Sensor activeSensor;
    private boolean isRunning;
    private boolean finished;

    // 조도: '기준 밝기 측정 → 가리기 → 다시 노출' 두 단계
    private float baseline = -1f;
    private int lightSamples = 0;
    private int lightStage = 0;
    private long coveredSince = 0;
    // 움직임: TYPE_LINEAR_ACCELERATION 우선, 없으면 TYPE_ACCELEROMETER에서 중력 제거
    private float[] gravity = new float[3];
    private boolean shakeArmed = true;
    private long lastShake = 0;
    private int shakeCount = 0;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        mission = getIntent().getIntExtra("mission", -1);
        if (!MissionData.valid(mission)) { finish(); return; }
        setContentView(R.layout.activity_mission_detail);
        ((TextView)findViewById(R.id.detailNumber)).setText(String.format(Locale.KOREA, "MISSION %02d", mission+1));
        ((TextView)findViewById(R.id.detailTitle)).setText(MissionData.TITLES[mission]);
        ((TextView)findViewById(R.id.detailPlace)).setText(MissionData.PLACES[mission] + " · " + MissionData.INPUTS[mission]);
        ((TextView)findViewById(R.id.detailStory)).setText(MissionData.STORIES[mission]);
        ((TextView)findViewById(R.id.detailLearning)).setText("학습 목표: " + MissionData.LEARNING[mission]);
        status = findViewById(R.id.detailStatus);
        statusDetail = findViewById(R.id.detailStatusHelp);
        meter = findViewById(R.id.missionMeter);
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        Button buttonSensor = findViewById(R.id.buttonSensor);
        buttonSensor.setOnClickListener(v -> startSensor());
        findViewById(R.id.buttonDemo).setOnClickListener(v -> showResult(true));

        if (mission == 1) {
            buttonSensor.setText("조도센서 미션 시작");
            status.setText("빛을 가렸다가 다시 비춰 보세요");
        } else if (mission == 4) {
            buttonSensor.setText("가속도센서 미션 시작");
            status.setText("가볍게 5번 움직여 게이지 채우기");
        } else {
            buttonSensor.setEnabled(false);
            buttonSensor.setText("기능 구현 예정");
            status.setText("이 미션은 화면·스토리 설계 단계입니다.");
            statusDetail.setText("실제 카메라/음성/방향 판정은 이후 개발합니다. 하단 버튼은 발표용 시연입니다.");
        }
        if (ProgressStore.isCompleted(this, mission)) {
            statusDetail.setText("이 기기에서 실제 센서 미션을 완료한 기록이 있습니다.");
        }
    }

    private void startSensor() {
        if (mission != 1 && mission != 4) return;
        stopSensor();
        finished = false;
        baseline = -1f; lightSamples = 0; lightStage = 0; coveredSince = 0;
        shakeCount = 0; shakeArmed = true; lastShake = 0;
        gravity = new float[3]; meter.setProgress(0);
        int type = mission == 1 ? Sensor.TYPE_LIGHT : Sensor.TYPE_LINEAR_ACCELERATION;
        activeSensor = manager.getDefaultSensor(type);
        if (mission == 4 && activeSensor == null) activeSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (activeSensor == null) {
            status.setText("이 기기는 해당 센서를 지원하지 않습니다.");
            statusDetail.setText("하단 시연 버튼은 실제 완료로 기록되지 않습니다.");
            return;
        }
        isRunning = manager.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_GAME);
        status.setText(isRunning ? "센서 측정 중" : "센서 등록에 실패했습니다.");
        statusDetail.setText(isRunning ? (mission == 1 ? "빛이 있는 곳에서 시작한 뒤 센서 부분을 가려 주세요." : "제자리에서 휴대전화를 가볍게 움직이세요.") : "다시 시도해 주세요.");
    }
    @Override public void onSensorChanged(SensorEvent event) {
        if (!isRunning || finished) return;
        if (mission == 1) handleLight(event.values[0]);
        if (mission == 4) handleMotion(event);
    }
    private void handleLight(float lux) {
        if (lightSamples < 8) {
            baseline = (baseline < 0 ? lux : baseline * .75f + lux * .25f);
            lightSamples++;
            if (lightSamples == 8 && baseline < 3f) {
                status.setText("주변이 너무 어둡습니다. 밝은 곳에서 다시 시작해 주세요.");
                stopSensor();
            } else statusDetail.setText("기준 밝기 측정 중: " + Math.round(lux) + " lux");
            return;
        }
        if (lightStage == 0) {
            status.setText("1단계: 조도센서를 손으로 가려 주세요");
            if (lux <= baseline * .3f) {
                if (coveredSince == 0) coveredSince = SystemClock.elapsedRealtime();
                if (SystemClock.elapsedRealtime() - coveredSince > 800) {
                    lightStage = 1; meter.setProgress(50);
                    status.setText("2단계: 손을 떼고 다시 밝게 해 주세요");
                }
            } else coveredSince = 0;
        } else if (lux >= baseline * .7f) {
            meter.setProgress(100);
            succeed();
        }
    }
    private void handleMotion(SensorEvent event) {
        float dx, dy, dz;
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            dx = event.values[0]; dy = event.values[1]; dz = event.values[2];
        } else {
            for (int k=0; k<3; k++) gravity[k] = .8f * gravity[k] + .2f * event.values[k];
            dx = event.values[0]-gravity[0]; dy = event.values[1]-gravity[1]; dz = event.values[2]-gravity[2];
        }
        double motion = Math.sqrt(dx*dx+dy*dy+dz*dz);
        long now = SystemClock.elapsedRealtime();
        if (motion < 1.2) shakeArmed = true;
        if (motion > 3.0 && shakeArmed && now-lastShake > 550) {
            shakeArmed = false; lastShake = now;
            shakeCount++;
            meter.setProgress(shakeCount * 20);
            status.setText("움직임 감지 " + shakeCount + " / 5");
            if (shakeCount >= 5) succeed();
        }
    }
    private void succeed() {
        if (finished) return;
        finished = true;
        stopSensor();
        ProgressStore.complete(this, mission);
        status.setText("실제 센서 미션 성공!");
        showResult(false);
    }
    private void showResult(boolean demo) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("mission", mission);
        intent.putExtra("demo", demo);
        startActivity(intent);
    }
    private void stopSensor() {
        if (manager != null) manager.unregisterListener(this);
        isRunning = false;
    }
    @Override protected void onPause() { stopSensor(); super.onPause(); }
    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
