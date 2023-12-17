
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @Headers("Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImJlNzgyM2VmMDFiZDRkMmI5NjI3NDE2NThkMjA4MDdlZmVlNmRlNWMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbnllbnlhay1wcm9qZWN0LWRldiIsImF1ZCI6Im55ZW55YWstcHJvamVjdC1kZXYiLCJhdXRoX3RpbWUiOjE3MDI3NDkyMDUsInVzZXJfaWQiOiJuVTFaSWl5TXJIZzVRcTNHN2FKMHNkb1lyT3MyIiwic3ViIjoiblUxWklpeU1ySGc1UXEzRzdhSjBzZG9Zck9zMiIsImlhdCI6MTcwMjc0OTIwNSwiZXhwIjoxNzAyNzUyODA1LCJlbWFpbCI6InRlc3QxQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJ0ZXN0MUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.ZWVbecqR8wOs5a-cPjWJO8aVasptNFJo6fLFkzWe7ZnG2Ahcj9yiNxyimUeUHf_0mz5oVv6W7jDN6GHOJq3rMVS7kYbhncxm_MJ2vwbmhUtYW8uKgLtBf_VabznoQJqWIqMYCrzw1LAUX3HNZSbKP0t4GlwFX05l6Ojdv_KeAqt6lI4cf1-9kPLdRR-8JVxvWHk-MM06lkH4NbXgrm1xOQSuy1X_UJXdSpzO8TRXcbQaElFMDmciK2qaeA1YWw-hzha9xaYNlpJLltVg_VjJ3n-l48s8zOcY7DSyPONGi8wdl4cLdCjncFDuY2bEuZFBseo5xV5FtES0MlPjrvnTCg")
    @FormUrlEncoded
    @POST("diagnosis")
    suspend fun inputDiagnosis(
        @Field("weight") weight: Int,
        @Field("height") height: Int,
        @Field("sleepDuration") sleepDuration: Float,
        @Field("qualityOfSleep") qualityOfSleep: Int,
        @Field("physicalActivityLevel") physicalActivityLevel: Int,
        @Field("bloodPressure") bloodPressure: String,
        @Field("stressLevel") stressLevel: Int,
        @Field("heartRate") heartRate: Int,
        @Field("dailySteps") dailySteps: Int
    ): InputResponse

    @Headers("Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImJlNzgyM2VmMDFiZDRkMmI5NjI3NDE2NThkMjA4MDdlZmVlNmRlNWMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbnllbnlhay1wcm9qZWN0LWRldiIsImF1ZCI6Im55ZW55YWstcHJvamVjdC1kZXYiLCJhdXRoX3RpbWUiOjE3MDI3NDkyMDUsInVzZXJfaWQiOiJuVTFaSWl5TXJIZzVRcTNHN2FKMHNkb1lyT3MyIiwic3ViIjoiblUxWklpeU1ySGc1UXEzRzdhSjBzZG9Zck9zMiIsImlhdCI6MTcwMjc0OTIwNSwiZXhwIjoxNzAyNzUyODA1LCJlbWFpbCI6InRlc3QxQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJ0ZXN0MUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.ZWVbecqR8wOs5a-cPjWJO8aVasptNFJo6fLFkzWe7ZnG2Ahcj9yiNxyimUeUHf_0mz5oVv6W7jDN6GHOJq3rMVS7kYbhncxm_MJ2vwbmhUtYW8uKgLtBf_VabznoQJqWIqMYCrzw1LAUX3HNZSbKP0t4GlwFX05l6Ojdv_KeAqt6lI4cf1-9kPLdRR-8JVxvWHk-MM06lkH4NbXgrm1xOQSuy1X_UJXdSpzO8TRXcbQaElFMDmciK2qaeA1YWw-hzha9xaYNlpJLltVg_VjJ3n-l48s8zOcY7DSyPONGi8wdl4cLdCjncFDuY2bEuZFBseo5xV5FtES0MlPjrvnTCg")
    @GET("diagnosis")
    fun getalldiagnosis() : Call<List<GetDiagnosisResponseItem>>

    @GET("diagnosis/{id]")
    suspend fun getdetaildiagnosis(@Path("id") id: String) : GetDiagnosisResponseItem

    @Headers("Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImJlNzgyM2VmMDFiZDRkMmI5NjI3NDE2NThkMjA4MDdlZmVlNmRlNWMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbnllbnlhay1wcm9qZWN0LWRldiIsImF1ZCI6Im55ZW55YWstcHJvamVjdC1kZXYiLCJhdXRoX3RpbWUiOjE3MDI3NDkyMDUsInVzZXJfaWQiOiJuVTFaSWl5TXJIZzVRcTNHN2FKMHNkb1lyT3MyIiwic3ViIjoiblUxWklpeU1ySGc1UXEzRzdhSjBzZG9Zck9zMiIsImlhdCI6MTcwMjc0OTIwNSwiZXhwIjoxNzAyNzUyODA1LCJlbWFpbCI6InRlc3QxQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJ0ZXN0MUBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.ZWVbecqR8wOs5a-cPjWJO8aVasptNFJo6fLFkzWe7ZnG2Ahcj9yiNxyimUeUHf_0mz5oVv6W7jDN6GHOJq3rMVS7kYbhncxm_MJ2vwbmhUtYW8uKgLtBf_VabznoQJqWIqMYCrzw1LAUX3HNZSbKP0t4GlwFX05l6Ojdv_KeAqt6lI4cf1-9kPLdRR-8JVxvWHk-MM06lkH4NbXgrm1xOQSuy1X_UJXdSpzO8TRXcbQaElFMDmciK2qaeA1YWw-hzha9xaYNlpJLltVg_VjJ3n-l48s8zOcY7DSyPONGi8wdl4cLdCjncFDuY2bEuZFBseo5xV5FtES0MlPjrvnTCg")
    @DELETE("diagnosis/{id}")
    suspend fun deletediagnosis(@Path("id") id: String): DeleteResponse

    @GET("articles")
    fun getarticle() : Call<List<ArticleResponseItem>>
}