package es.voghdev.playbattlegrounds.common.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()?.newBuilder()?.addHeader("Authorization", "Bearer $apiKey")
                ?.addHeader("accept", "application/vnd.api+json")?.build()
                ?: Request.Builder().build()

        return chain?.proceed(request) ?: Response.Builder().build()
    }
}
