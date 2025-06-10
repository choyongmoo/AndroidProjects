package kr.ac.yuhan.cs.androidproject;

import android.content.Context;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
//    private static final String BASE_URL = "http://172.20.10.3:8080/api/";
    private static Retrofit retrofit;
    private static ApiService apiService;
    private static TokenManager tokenManager;

    public static void initTokenManager(Context context) {
        if (tokenManager == null) {
            tokenManager = new TokenManager(context.getApplicationContext());
            retrofit = null;
            apiService = null;
        }
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        String path = original.url().encodedPath();
                        if (!path.contains("/auth/login") && !path.contains("/auth/register")) {
                            if (tokenManager != null) {
                                String token = tokenManager.getToken();
                                if (token != null && !token.isEmpty()) {
                                    Log.d("RetrofitClient", "Authorization token: " + token);
                                    builder.header("Authorization", "Bearer " + token);
                                }
                            }
                        }

                        Request request = builder.build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(loggingInterceptor)
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

    public static String getToken(Context context) {
        if (tokenManager == null) {
            initTokenManager(context);
        }
        return tokenManager.getToken();
    }
}
