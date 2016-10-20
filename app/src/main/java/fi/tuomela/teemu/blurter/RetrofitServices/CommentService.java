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
    // Request URL can be updated dynamically using {}-blocks and annotating
    // the method parameter using @Path.
    @GET("blurts/{id}/comments/")
    Call<List<Comment>> comments(@Path("id") String id);

    // Use the @Body annotation to add an object in the HTTP request body.
    @POST("comments/")
    Call<Comment> createComment(@Body Comment comment);

}
