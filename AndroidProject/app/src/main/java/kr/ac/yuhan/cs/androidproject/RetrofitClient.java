package kr.ac.yuhan.cs.androidproject;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    private static Retrofit retrofit;
    private static ApiService apiService;
    private static TokenManager tokenManager;

    // 앱 시작 시 한 번만 호출해서 TokenManager 초기화
    public static void initTokenManager(Context context) {
        if (tokenManager == null) {
            tokenManager = new TokenManager(context.getApplicationContext());
            // 초기화 후 Retrofit 인스턴스 리셋 (토큰 적용 위해)
            retrofit = null;
            apiService = null;
        }
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        if (tokenManager != null) {
                            String token = tokenManager.getToken();
                            if (token != null && !token.isEmpty()) {
                                builder.header("Authorization", "Bearer " + token);
                            }
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofitInstance().create(ApiService.class);
        }
        return apiService;
    }
}
