package pl.droidsonroids.bootcamp.yo.api;

import java.util.List;

import pl.droidsonroids.bootcamp.yo.model.User;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {

    @GET("/api/users")
    Observable<List<User>> getUsers();

    @POST("/api/users/{user_id}/yo")
    Observable<Void> postYo(@Path("user_id") int userId, @Query("self_id") int selfId);

    @POST("/api/register")
    User register(@Query("name") String name, @Query("registration_id") String registrationId);

    ApiService API_SERVICE = new RestAdapter.Builder()
            .setEndpoint("https://dor-yo.herokuapp.com")
            .setLog(new AndroidLog("yo api"))
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build()
            .create(ApiService.class);
}
