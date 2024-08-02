package sv.edu.ues.crud_retrofit_node_js

import android.app.Application
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class App:Application() {
    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        private lateinit var self:App
        private val retrofit:Retrofit= getRetrofit()
        val webService by lazy { webservice() }
        val request by lazy { Request(webService)}
       private fun webservice():WebService=retrofit.create(WebService::class.java)
        private fun getRetrofit(): Retrofit {
          val mediaType=MimeType.APPLICATION_JSON.toMediaType()
            val json= Json {ignoreUnknownKeys=true}
            return Retrofit.Builder()
                .baseUrl(AppConstantes.BASE_URL)
                .addConverterFactory(json.asConverterFactory(mediaType))
                .build()

        }

    }
}