package com.appssuite.ai.image.generator.text.to.texttoimageai;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenAiService {
    static  OpenAiService openAiService;
    OpenAiApi openAiApi;

    private static final String BASE_URL = "https://api.openai.com/";


    public OpenAiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .build();

        openAiApi = retrofit.create(OpenAiApi.class);

    }
    public  static OpenAiService getOpenAiService() {
        if (openAiService == null) {
            openAiService = new OpenAiService();
        }
        return openAiService;
    }
}
