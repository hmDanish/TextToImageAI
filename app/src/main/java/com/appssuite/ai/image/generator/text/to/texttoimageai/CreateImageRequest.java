package com.appssuite.ai.image.generator.text.to.texttoimageai;


import com.google.gson.annotations.SerializedName;

/**
 * A request for OpenAi to create an image based on a prompt
 * All fields except prompt are optional
 * <p>
 * https://beta.openai.com/docs/api-reference/images/create
 */

public class CreateImageRequest {

    //Generated images can have a size of 256x256, 512x512, or 1024x1024 pixels



//    "model": "image-alpha-001",
//            "prompt": "generate a picture of a mountain",
//            "num_images": 1,
//            "size": "1024x1024",
//            "response_format": "url"

    /**
     * A text description of the desired image(s). The maximum length in 1000 characters.
     */
    @SerializedName("prompt")
    String prompt;

    /**
     * The number of images to generate. Must be between 1 and 10. Defaults to 1.
     */
    @SerializedName("num_images")
    Integer n;

    /**
     * The size of the generated images. Must be one of "256x256", "512x512", or "1024x1024". Defaults to "1024x1024".
     */
    @SerializedName("size")
    String size;

    /**
     * The format in which the generated images are returned. Must be one of url or b64_json. Defaults to url.
     */
    @SerializedName("response_format")
    String responseFormat;

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;
}
