package com.retrofitrxjava.retrofit2RxJava.exception;


import com.retrofitrxjava.constants.Constants;

/**
 * Created by Admin on 2019/6/22.
 */
public class ApiException extends RuntimeException {

    private int mErrorCode;
    private String errorMessage; //报错信息

    public String getErrorMessage(){
        return errorMessage;
    }

    public int getErrorCode(){
        return mErrorCode;
    }

    public ApiException(int errorCode,
                        String errorMessage) {
        super(errorMessage);
        this.mErrorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 判断是否是token失效
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        return mErrorCode == Constants.TOKEN_EXPRIED;
    }
}
