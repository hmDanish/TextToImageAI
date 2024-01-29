package com.appssuite.ai.image.generator.text.to.texttoimageai;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * An object with a list of image results.
 *
 * https://beta.openai.com/docs/api-reference/images
 */

public class ImageResult {

    /**
     * The creation time in epoch seconds.
     */
    @SerializedName("createdAt")
    Long createdAt;

    /**
     * List of image results.
     */
    @SerializedName("data")
    List<Image> data;

    public ImageResult(List<Image> data) {
        this.data = data;
    }
}
