/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.testapp.model.Thift.Util;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cpu10878-local
 */

// Crawl Image from PIXABAY api

public class ImageCrawler {

    public static void main(String[] agrv) throws Exception {
        getImageUrls("king");
    }

    public static ArrayList<String> getImageUrls(String word) throws Exception {
        String URL_str = "https://pixabay.com/api/?key=5368218-000cd4738c0900d32520529a9&q=" + word + "&image_type=photo&pretty=true";

        URL pixabay_API = new URL(URL_str);
        URLConnection yc = pixabay_API.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        String serverResponse = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            serverResponse += inputLine;
        }
        in.close();

        JSONObject serverResponseJObj = new JSONObject(serverResponse);
        JSONArray hits = serverResponseJObj.getJSONArray("hits");

        ArrayList<String> results = new ArrayList<>();

        for (int i = 0; i < hits.length(); i++) {
            JSONObject imgData = hits.getJSONObject(i);
            results.add(imgData.getString("previewURL"));
        }

        return results;
    }
}
