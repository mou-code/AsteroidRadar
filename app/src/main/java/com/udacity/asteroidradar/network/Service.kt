package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

/**
 *  The Api Service: gets the data from the api
 */
interface AsteroidApiService {
    @GET("neo/rest/v1/feed/")
    suspend fun getAsteroids(): String

    @GET("planetary/apod/")
    suspend fun getPicOfTheDay(): PictureOfDay
}

/** OkHttp: for logging output of sending and receiving
 * between the retrofit and the RESTFUL service
 * And Adding an interceptor between them
 */
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(
        run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
    )
    .addInterceptor { apiKeyAsQuery(it) }
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .client(httpClient)
    .build()

/** The Main Object of the Network throughout the whole application  **/
object AsteroidsApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }

}

/** The api key as interceptor  **/
private fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
    chain.request()
        .newBuilder()
        .url(
            chain.request().url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
        )
        .build()
)