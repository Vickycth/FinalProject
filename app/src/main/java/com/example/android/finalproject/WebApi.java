package com.example.android.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebApi {
    private static final String WebApi_ApiKeyId = "x-api-key";
    private static final String WebApi_ApiKeyValue = "6e00303e-5d97-4b00-af8b-66fb1b90f3c4";
    private static final String WebApi_SubId = "my-user-001";
    private static final String WebApiUrl_GetImage = "https://api.thecatapi.com/v1/images/search?format=json&mime_types=jpg";
    private static final String WebApiUrl_PostFavImage = "https://api.thecatapi.com/v1/favourites";
    private static final String WebApiUrl_DeleteFavImage = "https://api.thecatapi.com/v1/favourites/";
    private static final String WebApiUrl_GetFavImage = "https://api.thecatapi.com/v1/favourites";

    private static String getStringFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String inputLine;
        try {
            while ((inputLine = bufferedReader.readLine()) != null) {
                builder.append(inputLine);
            }
            return builder.toString();
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cat getCat() {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(WebApiUrl_GetImage);
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty(WebApi_ApiKeyId, WebApi_ApiKeyValue);
            conn.setRequestMethod("GET");
            inputStream = new BufferedInputStream(conn.getInputStream());
            // get response string.
            String responseString = getStringFromInputStream(inputStream);
            // parse json string.
            JSONArray catArray = new JSONArray(responseString);
            JSONObject catObj = catArray.getJSONObject(0);
            String imgUrl = String.valueOf(catObj.getString("url"));
            String catId = String.valueOf(catObj.getString("id"));
            conn.disconnect();
            return new Cat(imgUrl, catId);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<FavCat> getFavouriteCatList() {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(WebApiUrl_GetFavImage);
            conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty(WebApi_ApiKeyId, WebApi_ApiKeyValue);
            inputStream = new BufferedInputStream(conn.getInputStream());
            // get response string.
            String responseString = getStringFromInputStream(inputStream);
            // parse json string.
            JSONArray array = new JSONArray(responseString);
            int num = array.length();
            List<FavCat> favList = new ArrayList<>();
            for (int i=0; i<num; i++) {
                JSONObject favObj = array.getJSONObject(i);
                String favId = favObj.getString("id");
                JSONObject catObj = favObj.getJSONObject("image");
                String imgUrl = String.valueOf(catObj.getString("url"));
                String catId = String.valueOf(catObj.getString("id"));
                FavCat favCat = new FavCat(favId, imgUrl, catId);
                favList.add(favCat);
            }
            return favList;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void postCatFavourite(String imageId) {
        if (imageId == null) {
            return;
        }
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(WebApiUrl_PostFavImage);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty(WebApi_ApiKeyId, WebApi_ApiKeyValue);
            conn.setRequestMethod("POST");
            // write post data.
            JSONObject fav = new JSONObject();
            fav.put("image_id", imageId);
            fav.put("sub_id", WebApi_SubId);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(fav.toString());
            wr.flush();
            // get response string.
            inputStream = new BufferedInputStream(conn.getInputStream());
            String responseString = getStringFromInputStream(inputStream);
            // parse json string.
            JSONObject result = new JSONObject(responseString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static void deleteCatFavourite(String favId) {
        if (favId == null) {
            return;
        }
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(WebApiUrl_DeleteFavImage + favId);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty(WebApi_ApiKeyId, WebApi_ApiKeyValue);
            conn.setRequestMethod("DELETE");
            // get response string.
            inputStream = new BufferedInputStream(conn.getInputStream());
            String responseString = getStringFromInputStream(inputStream);
            // parse json string.
            JSONObject result = new JSONObject(responseString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static Bitmap getURLImage(String url) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        Bitmap bmp = null;
        try {
            URL myUrl = new URL(url);
            conn = (HttpURLConnection) myUrl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            inputStream = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(inputStream);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
