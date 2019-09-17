package com.retrofitrxjava.retrofit2RxJava.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2019/6/22.
 */
public class HttpStatus {

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * API是否请求失败
     * @return 失败返回true, 成功返回false (code=200)
     */
    public boolean isCodeInvalid() {
//        return code != Constants.WEB_RESP_CODE_SUCCESS;
        return code != 200 ;
    }
}
