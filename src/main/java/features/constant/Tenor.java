package features.constant;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Fork of https://github.com/Jg99/TenorGIFJava
//A very simple Tenor GIF Search API Wrapper that returns the URL.
//It can either pick a random GIF (see returnRandomGIF function) or the first GIF from the result.
//returnGIFs returns all GIFs.
//Please use the setAPIKey function before executing other functions.


public class Tenor {
    private static String API_KEY = "";
    private static String lastString = "", currString = "", parsedMedia = "";

    public static void setAPIKey(String apikey) {
        API_KEY = apikey;
    }

    public static String returnGIF(String searchTerm) {
        JSONObject searchResult = getSearchResults(searchTerm, 2);
        assert searchResult != null;
        JSONArray jsonArray = searchResult.getJSONArray("results");
        for (int i = 0; i < jsonArray.length(); i++) {

            String[] data = String.valueOf(jsonArray.getJSONObject(i)).split(",");
            for (String s : data) {

                currString = s;
                if (currString.contains("tinymp4")) {

                    parsedMedia = lastString.substring(lastString.indexOf(":") + 2, lastString.lastIndexOf("}") - 1);
                }
                lastString = s;
            }
        }
        return parsedMedia;
    }

    public static String returnRandomGIF(String searchTerm, int maxResults) {
        JSONObject searchResult = getSearchResults(searchTerm, maxResults);
        Random rand = new Random();
        ArrayList<String> list = new ArrayList<>();
        assert searchResult != null;
        JSONArray jsonArray = searchResult.getJSONArray("results");

        for (int i = 0; i < jsonArray.length(); i++) {

            String[] data = String.valueOf(jsonArray.getJSONObject(i)).split(",");
            for (String s : data) {

                currString = s;
                if (currString.contains("tinymp4")) {

                    list.add(lastString.substring(lastString.indexOf(":") + 2, lastString.lastIndexOf("}") - 1));
                }
                lastString = s;
            }
        }
        parsedMedia = list.get(rand.nextInt(list.size() - 1));
        return parsedMedia;
    }

    public static JSONObject getSearchResults(String searchTerm, int limit) {

        // make search request - using default locale of EN_US

        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=%3$s",
                searchTerm, API_KEY, limit);
        try {
            return get(url);
        } catch (IOException | JSONException ignored) {
        }
        return null;
    }

    private static JSONObject get(String url) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Handle failure
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                System.out.println(error);
                throw new ConnectException(error);
            }

            // Parse response
            return parser(connection);
        } catch (Exception ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }

    private static JSONObject parser(HttpURLConnection connection) throws JSONException {
        char[] buffer = new char[1024 * 4];
        int n;
        try (InputStream stream = new BufferedInputStream(connection.getInputStream())) {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return new JSONObject(writer.toString());
        } catch (Exception ignored) {
        }
        return new JSONObject("");
    }
}