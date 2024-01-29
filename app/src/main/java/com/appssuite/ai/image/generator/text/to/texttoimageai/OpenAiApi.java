package com.appssuite.ai.image.generator.text.to.texttoimageai;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAiApi {
    String apiKey = "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";

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer " + apiKey
    })
    @POST("v1/images/generations")
    Call<ImageResult> createImage(@Body CreateImageRequest createImageRequest);
}
