package uk.co.compendiumdev.drillreporting.api;

import com.google.gson.Gson;
import uk.co.compendiumdev.drillreporting.outputformats.FormatResponseTableAsMarkdownTable;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DrillQuery {
    private final String bodyContent;
    private final String url;
    URL requestUrl;

    public DrillQuery(final String bodyContent, final String url) {

        this.bodyContent = bodyContent;
        this.url = url;
        try {
            requestUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public DrillQueryResult post() {

        if(requestUrl==null){
            throw new RuntimeException("URL not set correctly");
        }

        final DrillQueryResult result = new DrillQueryResult();

        String messageFormat = "{\"queryType\" : \"SQL\", \"query\" : \"%s\"}";
        String body = String.format(messageFormat, bodyContent);

        result.setUrl(url);
        System.out.println(result.url());


        try {
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "drill-reporter");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            out.write(body.getBytes());

            int statusCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer responseBody = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                responseBody.append(inputLine);
            }
            in.close();

            result.setStatusCode(statusCode);
            result.setResponseBody(responseBody.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


//        final Response response = RestAssured.given().header("Content-Type", "application/json").
//                body(body).when().post(url).andReturn();
//
//        result.setStatusCode(response.statusCode());
//        result.setResponseBody(response.body().asString());

        System.out.println(result.statusCode());
        System.out.println(result.responseBody());

        if(result.statusCode()==200){
            result.setMarkdown(formatResponseTableAsMarkdown(result.responseBody()));
            System.out.println(result.markdown());
            result.setNumberOfRows(countNumberOfResults(result.responseBody()));
        }

        return result;
    }

    private String formatResponseTableAsMarkdown(final String json) {
        final Gson gson = new Gson();

        final DrillResponse payload = gson.fromJson(json, DrillResponse.class);

        return formatResponseTableAsMarkdown(payload);
    }

    private String formatResponseTableAsMarkdown(DrillResponse payload) {

        return new FormatResponseTableAsMarkdownTable().format(payload);
    }

    private int countNumberOfResults(final String json) {
        final Gson gson = new Gson();

        final DrillResponse payload = gson.fromJson(json, DrillResponse.class);

        return payload.rows.size();
    }
}
