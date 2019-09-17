package com.retrofitrxjava.retrofit2RxJava.Converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.retrofitrxjava.retrofit2RxJava.bean.HttpStatus;
import com.retrofitrxjava.retrofit2RxJava.exception.ApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Admin on 2019/6/22.
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        System.out.println("CustomGsonResponseBodyConverter--"+response);
        HttpStatus httpStatus = gson.fromJson(response, HttpStatus.class);
        if (httpStatus.isCodeInvalid()) {
            value.close();
            throw new ApiException(httpStatus.getCode(), httpStatus.getMessage());
        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
