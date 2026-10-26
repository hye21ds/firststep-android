package com.example.firststep;

import android.content.Context;
import android.content.SharedPreferences;

public final class ProgressStore {
    private static final String FILE = "firststep_progress";
    private ProgressStore() {}
    public static boolean isCompleted(Context context, int mission) {
        return context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .getBoolean("mission_" + mission, false);
    }
    public static void complete(Context context, int mission) {
        if (mission != 1 && mission != 4) return; // 1차 개발: M02 조도, M05 가속도만 실제 성공 판정
        SharedPreferences prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("mission_" + mission, true).apply();
    }
    public static int count(Context context) {
        int result = 0;
        for (int i = 0; i < MissionData.TITLES.length; i++) {
            if (isCompleted(context, i)) result++;
        }
        return result;
    }
}
