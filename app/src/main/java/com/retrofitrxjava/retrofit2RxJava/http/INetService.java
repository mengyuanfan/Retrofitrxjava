package com.retrofitrxjava.retrofit2RxJava.http;



import com.retrofitrxjava.constants.GlobalConstantUrl;
import com.retrofitrxjava.main.MainBean;
import com.retrofitrxjava.main.model.MainModel;
import com.retrofitrxjava.retrofit2RxJava.SuccessBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * service统一接口数据
 */
public interface INetService {

    //POST请求  --(post无参时)
    @POST(GlobalConstantUrl.mainUrl)
    Observable<MainBean> getMainDataPost();

      //POST请求  --(post传参时)
//    @FormUrlEncoded
//    @POST(GlobalConstantUrl.mainUrl)
//    Observable<MainBean> getMainDataPost(@Field("page") int page,
//                                         @Field("size") int size,
//                                         @Field("order") String order,
//                                         @Field("lat") String lat,
//                                         @Field("lng") String lng);

    //个人资料---(post上传图片)
//    @Multipart
//    @POST(GlobalConstantUrl.mainUrl)
//    Observable<String> setPersonHeadImagePost(
//            @Part("file\"; filename=\"avatar.jpg") RequestBody parts,
//            @Part("rd3_key") String rd3_key);

    @FormUrlEncoded
    @POST("mobile/index.php?act=member_cart&op=cart_add")
    Observable<SuccessBean> getAddCartGoodsPost(@FieldMap Map<String, String> map);

    //POST请求
    @FormUrlEncoded
    @POST("/user/login")
    Observable<SuccessBean> getVerfcationCodePostMap(@FieldMap Map<String, String> map);

    //GET请求，设置缓存
//    @Headers("Cache-Control: public," + OtherConstants.CACHE_CONTROL_CACHE)
    @GET("bjws/app.user/login")
    Observable<SuccessBean> getVerfcationGetCache(@Query("tel") String tel,
                                                  @Query("password") String password);

    //GET请求 查询网络的Cache-Control设置。不使用缓存
//    @Headers("Cache-Control: public," + OtherConstants.CACHE_CONTROL_NETWORK)
    @GET("bjws/app.menu/getMenu")
    Observable<SuccessBean> getMainMenu();


}
