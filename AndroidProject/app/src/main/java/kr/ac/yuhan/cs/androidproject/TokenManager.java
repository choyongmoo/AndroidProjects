package kr.ac.yuhan.cs.androidproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

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

    // 토큰 삭제
    public void clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }

    // 🔥 토큰에서 userId (sub claim) 추출
    public String getUserIdFromToken() {
        String token = getToken();
        if (token == null) return null;

        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
            JSONObject json = new JSONObject(payload);
            return json.getString("sub"); // 서버에서 JWT에 넣은 userId가 "sub"일 경우
        } catch (JSONException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}

