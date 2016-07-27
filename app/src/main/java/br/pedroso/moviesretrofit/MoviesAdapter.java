package br.pedroso.moviesretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.pedroso.moviesretrofit.rest.model.MovieInfo;

/**
 * Created by felip on 20/07/2016.
 */
public class MoviesAdapter extends ArrayAdapter<MovieInfo> {

    public MoviesAdapter(Context context) {
        super(context, R.layout.grid_item_movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        MovieInfo movieInfo = getItem(position);


        String imageUrl = getContext().getString(R.string.tmdb_image_base_url) + movieInfo.getPosterPath();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.moviePoster);
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.ic_photo).error(R.drawable.ic_error).into(imageView);

        TextView titleView = (TextView) convertView.findViewById(R.id.movieTitle);
        titleView.setText(movieInfo.getTitle());

        return convertView;
    }
}
