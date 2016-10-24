package fi.tuomela.teemu.blurtermongo.RetrofitServices;

import java.util.List;

import fi.tuomela.teemu.blurtermongo.Models.Blurt;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Teemu on 18.10.2016.
 *
 */

public interface BlurtService {
    // Annotations provide the request method and relative path.
    @GET("blurts/")
    Call<List<Blurt>> blurts();

    @POST("blurts/")
    Call<Blurt> createBlurt(@Body Blurt blurt);

}
