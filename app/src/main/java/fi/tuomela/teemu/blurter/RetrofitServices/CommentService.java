package fi.tuomela.teemu.blurter.RetrofitServices;

import java.util.List;

import fi.tuomela.teemu.blurter.Models.Comment;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Teemu on 18.10.2016.
 *
 */

public interface CommentService {
    @GET("blurts/{id}/comments/")
    Call<List<Comment>> comments(@Path("id") String id);

    @POST("comments/")
    Call<Comment> createComment(@Body Comment comment);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.10.50:5000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
