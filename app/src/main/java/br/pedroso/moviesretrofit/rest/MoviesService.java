package br.pedroso.moviesretrofit.rest;

import br.pedroso.moviesretrofit.rest.model.MovieResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by felip on 20/07/2016.
*/
public interface MoviesService {
    String API_KEY_QUERY_PARAMETER = "api_key";

    @GET("movie/upcoming")
    Call<MovieResults> listUpcomingMovies();

    @GET("movie/{movieId}/similar")
    Call<MovieResults> listSimilarMovies(@Path("movieId") Integer movieId);
}
