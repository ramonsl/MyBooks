package com.example.ramonsl.mybooks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ramonsl on 30/04/2018.
 */

public class Books implements Serializable {
    private String mTitle;
    private String mPublisher;
    private String mAutor;
    private String mDescription;

    private int mPageCount;
    private String mThumbnail;

    public Books(String mTitle, String mPublisher, String mAutor, String mDescription, int mPageCount, String mThumbnail) {
        this.mTitle = mTitle;
        this.mPublisher = mPublisher;
        this.mAutor = mAutor;
        this.mDescription = mDescription;
        this.mPageCount = mPageCount;
        this.mThumbnail = mThumbnail;
    }


    public static Books getBooks(JSONObject jsonBook) {

        String title = "";
        String publisher = "";
        String autor = "";
        String description = "";
        int pageCount = 0;
        String thumbnail = "";
        try {

            JSONObject volumeInfo = jsonBook.getJSONObject("volumeInfo");
            title = validJson(volumeInfo, title, "title");
            description = validJson(volumeInfo, description, "description");
            publisher = validJson(volumeInfo, publisher, "publisher");
            pageCount = validJson(volumeInfo, pageCount, "pageCount");
            autor = validJsonAuthors(volumeInfo, autor, "authors");

            JSONObject jsonimage = validJson(volumeInfo, volumeInfo, "imageLinks");

            if (jsonimage != null) {
                thumbnail = jsonimage.getString("smallThumbnail");
                Books b = new Books(title, publisher, autor, description, pageCount, thumbnail);
                return b;
            }


        } catch (Exception ex) {
            Log.e("Error", "Json invalido");
            return null;
        }
        return null;
    }

    private static String validJson(JSONObject json, String item, String key) {
        try {
            item = json.getString(key);
        } catch (JSONException a) {
            item = "no data";
        }
        return item;

    }

    private static int validJson(JSONObject json, int item, String key) {
        try {
            item = json.getInt(key);
        } catch (JSONException a) {
            item = 0;
        }
        return item;

    }

    private static JSONObject validJson(JSONObject json, JSONObject item, String key) {
        try {
            item = json.getJSONObject(key);
        } catch (JSONException a) {
            item = null;
        }
        return item;

    }

    private static String validJsonAuthors(JSONObject json, String item, String key) {
        JSONArray jsonAutores = null;
        item = "";
        try {
            jsonAutores = json.getJSONArray(key);
            for (int i = 0; i < jsonAutores.length(); i++) {
                item += jsonAutores.getString(0) + " ";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;

    }

    public String getTitle() {
        return mTitle;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getAutor() {
        return mAutor;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    @Override
    public String toString() {
        return mTitle;
    }


}
