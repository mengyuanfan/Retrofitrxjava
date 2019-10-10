# Retrofitrxjava

retrofit2 Rxjava 的MVP网络请求


    //Retrofit帮助类

    public class RetrofitHelper {

        private static OkHttpClient mClient;
        private Retrofit mRetrofit;

        private RetrofitHelper() {
            mClient = OkHttpClientHelper.getInstance().getOkHttpClient();
        }

        private static RetrofitHelper helper;

        //单例 保证对象唯一
        public static RetrofitHelper getInstance() {
            if (helper == null) {
                synchronized (RetrofitHelper.class) {
                    if (helper == null) {
                        helper = new RetrofitHelper();
                    }
                }
            }
            return helper;
        }

        //获取Retrofit对象
        public Retrofit getRetrofit() {

            if (mClient == null) {

                mClient = OkHttpClientHelper.getInstance().getOkHttpClient();
            }

            if (mRetrofit == null) {
                mRetrofit = new Retrofit.Builder()
                        .baseUrl(GlobalConstantUrl.BASE_SERVER + "/")
                        //TODO 修改--java.lang.IllegalStateException: Fatal Exception thrown on Scheduler.Worker thread.
                        .addConverterFactory(ScalarsConverterFactory.create())//首先判断是否需要转换成字符串，简单类型
                        .addConverterFactory(CustomGsonConverterFactory.create(new Gson()))  //再将转换成bean 添加Gson支持(自定义)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  //添加RxJava支持
                        .client(mClient)                                            //关联okhttp
                        .build();
            }
            return mRetrofit;
        }

        public class NullOnEmptyConverterFactory extends Converter.Factory {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                                    Annotation[] annotations,
                                                                    Retrofit retrofit) {
                final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter
                        (this, type, annotations);
                return new Converter<ResponseBody,Object>() {
                    @Override
                    public Object convert(ResponseBody body) throws IOException {
                        if (body.contentLength() == 0) return null;
                        return delegate.convert(body);
                    }
                };
            }
        }

    }


    //Okhttp帮助类

    public class OkHttpClientHelper {

        private final Cache cache;
        private OkHttpClient mClient;
        private final  static  long TIMEOUT = 10000;  //超时时间

        private OkHttpClientHelper(){
            cache = CacheHelper.getInstance().getCache();
        }

        private static OkHttpClientHelper clientHelper;

        public static OkHttpClientHelper getInstance(){
            if(clientHelper==null){
                synchronized (OkHttpClientHelper.class){
                    if(clientHelper==null){
                        clientHelper = new OkHttpClientHelper();
                    }
                }
            }
            return clientHelper;
        }

        //获取OKHttpClicent对象
        public OkHttpClient getOkHttpClient(){

            if(mClient ==null) {
                mClient = new OkHttpClient.Builder()
                        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                        .cache(cache)      //设置缓存(可有可无)
                        .build();
            }
            return mClient;
        }
    }


    // 网络请求工具类


    public class HttpUtils {

        private static INetService mService = getService();


        /**
         * 首页--数据
         */
        public static void requestGeMainDataByPost(Subscriber subscriber) {
            setSubscriber(mService.getMainDataPost(), subscriber);
        }

        //订阅事件
        public static <T> void setSubscriber(Observable<T> observable, Subscriber<T> subscriber) {
            observable.subscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.newThread())//子线程访问网络
                    .observeOn(AndroidSchedulers.mainThread())  //回调到主线程
                    .subscribe(subscriber);
        }

        //获取服务对象
        private static INetService getService() {
            if (mService == null) {
                mService = RetrofitHelper.getInstance()
                        .getRetrofit()
                        .create(INetService.class);
            }
            return mService;
        }

      }


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

