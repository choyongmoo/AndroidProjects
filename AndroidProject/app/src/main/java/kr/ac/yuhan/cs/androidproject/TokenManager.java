package kr.ac.yuhan.cs.androidproject;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "token";

    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // 토큰 저장
    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    // 토큰 가져오기
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    // 토큰 삭제 (로그아웃 시 사용 가능)
    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
