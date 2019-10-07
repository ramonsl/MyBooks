package com.example.ramonsl.mybooks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ramonsl on 30/07/2019.
 */

public class BooksHttp {

    public static String URL;

    public static String setUrl(String termo, int maxResults) {
        StringBuilder http = new StringBuilder();
        http.append("https://www.googleapis.com/books/v1/volumes?q=");
        http.append(termo);
        http.append("&maxResults=");
        http.append(maxResults);
        URL = http.toString();
        return URL;
    }

    private static HttpURLConnection connectar(String urlWebservice) {

        final int SEGUNDOS = 10000;

        try {
            URL url = new URL(urlWebservice);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setReadTimeout(10 * SEGUNDOS);
            conexao.setConnectTimeout(15 * SEGUNDOS);
            conexao.setRequestMethod("GET");
            conexao.setDoInput(true);
            conexao.setDoOutput(false);
            conexao.connect();
            return conexao;

        } catch (IOException e) {
            Log.d("ERRO", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static boolean hasConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }


    public static ArrayList<Books> loadBooks() {


        try {
            HttpURLConnection connection = connectar(URL);
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(inputStream));
               ArrayList<Books>  bookList =readJson(json);
               return bookList;
            }

        } catch (Exception e) {
            Log.d("ERRO", e.getMessage());
        }
        return null;
    }




    public static ArrayList<Books> readJson(JSONObject json) {


        ArrayList<Books> myBooks = new ArrayList<>();
        try {
            JSONArray jsonItens = json.getJSONArray("items");
            for (int i = 0; i < jsonItens.length(); i++) {

                Books book = Books.getBooks(jsonItens.getJSONObject(i));

                if (book != null) {
                    myBooks.add(book);
                }

            }
        } catch (JSONException e) {

            Log.d("Json", e.getMessage());
            e.printStackTrace();
        }
        return myBooks;

    }

    private static String bytesParaString(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int byteslidos;
        try {
            while ((byteslidos = inputStream.read(buffer)) != -1) {
                bufferzao.write(buffer, 0, byteslidos);

            }
            return new String(bufferzao.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
