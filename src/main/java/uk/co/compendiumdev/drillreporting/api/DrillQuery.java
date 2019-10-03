package uk.co.compendiumdev.drillreporting.api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import uk.co.compendiumdev.drillreporting.outputformats.FormatResponseTableAsMarkdownTable;

public class DrillQuery {
    private final String bodyContent;
    private final String url;

    public DrillQuery(final String bodyContent, final String url) {

        this.bodyContent = bodyContent;
        this.url = url;
    }

    // TODO remove REST Assured for this request
    public DrillQueryResult post() {

        final DrillQueryResult result = new DrillQueryResult();

        String messageFormat = "{\"queryType\" : \"SQL\", \"query\" : \"%s\"}";
        String body = String.format(messageFormat, bodyContent);

        result.setUrl(url);
        System.out.println(result.url());

        final Response response = RestAssured.given().header("Content-Type", "application/json").
                body(body).when().post(url).andReturn();

        result.setStatusCode(response.statusCode());
        result.setResponseBody(response.body().asString());

        System.out.println(result.statusCode());
        System.out.println(result.responseBody());

        if(result.statusCode()==200){
            result.setMarkdown(formatResponseTableAsMarkdown(response.body().asString()));
            System.out.println(result.markdown());
            result.setNumberOfRows(countNumberOfResults(response.body().asString()));
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
