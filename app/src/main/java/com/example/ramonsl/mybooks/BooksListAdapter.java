package com.example.ramonsl.mybooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ramonsl on 02/05/2018.
 */

public class BooksListAdapter extends ArrayAdapter<Books> {


    public BooksListAdapter(@NonNull Context context, @NonNull List<Books> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Books books = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_book, null);
            holder = new ViewHolder();
            holder.imgCapa = (ImageView) convertView.findViewById(R.id.imgCapa);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitulo);
            holder.txtDescripition = (TextView) convertView.findViewById(R.id.txtDescripition);
            holder.txtEditora = (TextView) convertView.findViewById(R.id.txtEditora);
            holder.txtAutores = convertView.findViewById(R.id.txtAutores);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.get().load(books.getThumbnail()).into(holder.imgCapa);
        holder.txtTitle.setText(books.getTitle());
        holder.txtEditora.setText(books.getPublisher());
        holder.txtDescripition.setText(String.valueOf(books.getDescription().substring(0, 250).concat("...LEIA MAIS")));
        holder.txtAutores.setText(books.getAutor());
        return convertView;
    }

    static class ViewHolder {
        ImageView imgCapa;
        TextView txtTitle;
        TextView txtDescripition;
        TextView txtEditora;
        TextView txtAutores;

    }
}
