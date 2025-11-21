package com.bubble.probubblebopling.ror.data.repo

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.lang.Exception

interface ChickenApi {
    @Headers("Content-Type: application/json")
    @POST("config.php")
    fun getClient(
        @Body jsonString: JsonObject,
    ): Call<com.bubble.probubblebopling.ror.domain.model.ProBubbleBoPlingEntity>
}


private const val CHICKEN_MAIN = "https://probubbllebopling.com/"
class ChickenRepository {

    suspend fun chickenGetClient(
        chickenParam: com.bubble.probubblebopling.ror.domain.model.ChickenParam,
        chickenConversion: MutableMap<String, Any>?
    ): com.bubble.probubblebopling.ror.domain.model.ProBubbleBoPlingEntity? {
        val gson = Gson()
        val api = chickenGetApi(CHICKEN_MAIN, null)

        val chickenJsonObject = gson.toJsonTree(chickenParam).asJsonObject
        chickenConversion?.forEach { (key, value) ->
            val element: JsonElement = gson.toJsonTree(value)
            chickenJsonObject.add(key, element)
        }
        return try {
            val chickenRequest: Call<com.bubble.probubblebopling.ror.domain.model.ProBubbleBoPlingEntity> = api.getClient(
                jsonString = chickenJsonObject,
            )
            val chickenResult = chickenRequest.awaitResponse()
            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: Result code: ${chickenResult.code()}")
            if (chickenResult.code() == 200) {
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: Get request success")
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: Code = ${chickenResult.code()}")
                Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: ${chickenResult.body()}")
                chickenResult.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: Get request failed")
            Log.d(_root_ide_package_.com.bubble.probubblebopling.ror.presentation.app.ProBubbleBoPlingApp.Companion.CHICKEN_MAIN_TAG, "Retrofit: ${e.message}")
            null
        }
    }


    private fun chickenGetApi(url: String, client: OkHttpClient?) : ChickenApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client ?: OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create()
    }


}
