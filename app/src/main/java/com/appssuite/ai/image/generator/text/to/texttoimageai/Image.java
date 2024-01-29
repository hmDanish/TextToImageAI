package com.appssuite.ai.image.generator.text.to.texttoimageai;



import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * An object containing either a URL or a base 64 encoded image.
 *
 * https://beta.openai.com/docs/api-reference/images
 */

public class Image {
    /**
     * The URL where the image can be accessed.
     */
    String url;


    /**
     * Base64 encoded image string.
     */
    @JsonProperty("b64_json")
    String b64Json;

    public Image(String url) {
        this.url = url;
    }
}
