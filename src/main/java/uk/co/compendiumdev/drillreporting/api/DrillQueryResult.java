package uk.co.compendiumdev.drillreporting.api;

public class DrillQueryResult {
    private String url="";
    private int statusCode=0;
    private String responseBody="";
    private String markdown="";
    private int numberOfRows;
    private DrillResponse payload;

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseBody(final String body) {
        this.responseBody = body;
    }

    public int statusCode() {
        return statusCode;
    }

    public String responseBody() {
        return responseBody;
    }

    public String url() {
        return url;
    }

    public void setMarkdown(final String formatResponseTableAsMarkdown) {
        this.markdown = formatResponseTableAsMarkdown;
    }

    public String markdown() {
        return this.markdown;
    }

    public void setNumberOfRows(final int rows) {
        this.numberOfRows = rows;
    }

    public int numberOfRows() {
        return numberOfRows;
    }

    public void setPayload(final DrillResponse payload) {
        this.payload = payload;
    }

    public DrillResponse payload(){
        return this.payload;
    }

    public DrillQueryResult asLimitedResults(final int limitResults) {
        // create a clone but limit the results
        DrillQueryResult clone = new DrillQueryResult();
        clone.setPayload(payload.withLimitedRows(limitResults));
        clone.setStatusCode(statusCode);
        clone.setResponseBody(responseBody);
        clone.setNumberOfRows(numberOfRows); // this could be dangerous as it is the initial number not the limited number
        clone.setUrl(url);
        return clone;
    }
}
