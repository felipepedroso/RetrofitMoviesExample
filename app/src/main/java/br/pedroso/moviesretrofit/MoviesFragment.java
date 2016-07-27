package br.pedroso.moviesretrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.IOException;

import br.pedroso.moviesretrofit.rest.MoviesService;
import br.pedroso.moviesretrofit.rest.model.MovieResults;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by felip on 20/07/2016.
 */
public class MoviesFragment extends Fragment implements Callback<MovieResults> {
    private MoviesAdapter moviesAdapter;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.grid_movies);
        moviesAdapter = new MoviesAdapter(getContext());
        gridView.setAdapter(moviesAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMovies();
    }

    private void updateMovies() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl originalUrl = originalRequest.url();

                HttpUrl urlWithApiKey = originalUrl.newBuilder()
                        .addQueryParameter(MoviesService.API_KEY_QUERY_PARAMETER, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                Request modifiedRequest = originalRequest.newBuilder()
                        .url(urlWithApiKey)
                        .build();

                okhttp3.Response response = chain.proceed(modifiedRequest);

                return response;
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient httpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        MoviesService moviesService = retrofit.create(MoviesService.class);

        moviesService.listUpcomingMovies().enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
        moviesAdapter.clear();
        moviesAdapter.addAll(response.body().getResults());
    }

    @Override
    public void onFailure(Call<MovieResults> call, Throwable t) {
        // Do nothing for a while
        moviesAdapter.clear();
    }
}
