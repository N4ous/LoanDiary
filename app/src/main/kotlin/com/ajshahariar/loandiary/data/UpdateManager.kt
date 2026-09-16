package com.ajshahariar.loandiary.data

import com.ajshahariar.loandiary.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class AppUpdate(
    val versionCode: Int,
    val versionName: String,
    val updateMessage: String,
    val downloadUrl: String
)

@Singleton
class UpdateManager @Inject constructor() {
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    
    // This should point to your GitHub raw version.json
    private val updateUrl = "https://raw.githubusercontent.com/N4ous/LoanDiary/main/version.json"

    suspend fun checkForUpdate(): AppUpdate? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(updateUrl).build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: return@withContext null
                val update = json.decodeFromString<AppUpdate>(responseBody)
                
                if (update.versionCode > BuildConfig.VERSION_CODE) {
                    update
                } else {
                    null
                }
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
