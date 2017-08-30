package com.hao.summaryproject.di.module;

import com.hao.summaryproject.BuildConfig;
import com.hao.summaryproject.app.Constants;
import com.hao.summaryproject.http.api.APIService;
import com.hao.summaryproject.util.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hao.summaryproject.app.Constants.BASE_URL;

/**
 * 网络请求模块
 * Created by liuzhu
 * on 2017/8/22.
 */
@Module
public class HttpModule {

    private static final int DEFAULT_TIME = 10;

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //在debug模式下打印网络日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        File file = new File(Constants.CACHE_PATH);
        Cache cache = new Cache(file, 20 * 1024 * 1024);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!SystemUtil.isNetWorkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                Response responseLatest;
                if (SystemUtil.isNetWorkConnected()) {
                    //有网络时不用缓存
                    int maxAge = 0;
                    responseLatest = response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    //无网络时用缓存(需存在缓存)
                    int maxStale = 60 * 60 * 24 * 5;
                    responseLatest = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return responseLatest;
            }
        };

        //设置缓存
        builder.addNetworkInterceptor(interceptor);
        builder.addInterceptor(interceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public APIService provideService() {
        return provideRetrofit().create(APIService.class);
    }

}
