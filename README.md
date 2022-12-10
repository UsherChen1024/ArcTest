# ArcTest
## 运行方式
clone工程，然后直接运行
## 实现效果
<img width="185" alt="image" src="https://user-images.githubusercontent.com/25892316/206825801-23c91dd1-1606-4a3b-8e07-3d9db1381c13.png">
<img width="240" alt="image" src="https://user-images.githubusercontent.com/25892316/206825882-54b5daf2-7196-414b-b1dd-0d09a28b7939.png">
## 异常情况处理
### 网络不可用
```
    private fun fetchData() {
        if (!isOnline(this)) {
            Toast.makeText(this, "网络连接不可用", Toast.LENGTH_SHORT).show()
        }
        mainViewModel.fetchPosters()
    }
```
采用toast提示用户，由于可能存在缓存，仍旧尝试获取海报数据。
### 网络请求异常
```
viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.d("MainViewModel", "response error: ${throwable.message}")
            _toastEvent.postValue(throwable.message)
        })
```
对网络请求进行异常处理，出现请求失败时，通过toast的方式提示用户。
### 数据为null的情况
```
data class Frontmatter(
    val banner: Banner,
    val categories: List<String>?,
    val date: String?,
    val draft: Any,
    val language: String?,
    val path: String?,
    val tags: List<String>?,
    val title: String?
)
```
由于服务器的数据有可能存在空值，在model定义和使用时，都对空值进行兼容。但对于banner字段，由于是核心数据，不支持空值，如果是空值则抛出异常。
## 性能相关
### posts列表缓存
#### 开启okhttp缓存
```
       val file = File(MainApp.instance.cacheDir, "okhttp_cache")
        val cache = Cache(file, 10 * 1024 * 1024)

        val okHttpClient = OkHttpClient().newBuilder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .addInterceptor(logInterceptor)
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d("NetworkInterceptor", "real request url: ${chain.request().url}")
                    return chain.proceed(chain.request())
                }
            })
            .build()
```
由于是get请求，通过配置OkHttp的缓存机制，打开okhttp的缓存，可以实现根据http缓存规则进行缓存。
#### 效果检测
```
           .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    Log.d("NetworkInterceptor", "real request url: ${chain.request().url}")
                    return chain.proceed(chain.request())
                }
            })
```
通过NetworkInterceptor的打印可以确认是否真实发起了网络请求，通过log可以确认缓存生效。
### 图片缓存
```
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        val memoryCacheSize = maxMemory / 8
        builder.setMemoryCache(LruResourceCache(memoryCacheSize.toLong()))
```
使用glide进行图片的加载，配置缓存为应用缓存的8分之一。
### 图片内存
图片的内存 = width * height * 单像素字节数
#### 图片格式
```
        builder.setDefaultRequestOptions(
            RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .disallowHardwareConfig()
        )
```
Glide新版本默认图片格式为ARGB8888，对于图片质量不敏感的应用，可以配置为RGB555，对于本身不支持Alpha通道的图片可以节约一半内存。
#### 大图片
对于大图片一般有两种处理方式：
1. 使用inSampleSize的方式缩小图片的宽高。
2. 使用BitmapRegionDecoder加载大图片的部分区域，根据手势去移动显示的区域，来查看其它部分。用于如微博大图片浏览。
这里借助于Glide，采用的是方式1，根据ImageView的宽高来缩小图片的宽高。
