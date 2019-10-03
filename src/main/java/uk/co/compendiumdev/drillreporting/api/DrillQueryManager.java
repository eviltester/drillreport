package uk.co.compendiumdev.drillreporting.api;

import com.google.gson.Gson;
import uk.co.compendiumdev.drillreporting.linemacro.ParsedMacro;
import uk.co.compendiumdev.drillreporting.outputformats.DrillResponseTextFormat;
import uk.co.compendiumdev.drillreporting.outputformats.FormatResponseTableAsMarkdownCountTable;
import uk.co.compendiumdev.drillreporting.outputformats.FormatResponseTableAsMarkdownList;
import uk.co.compendiumdev.drillreporting.outputformats.FormatResponseTableAsMarkdownTable;
import uk.co.compendiumdev.drillreporting.virtualquery.VirtualQuery;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class DrillQueryManager {
    private final File queryPath;
    private final Map<String, String> templateParams;
    private final Map<String, DrillQueryResult> responseCache;
    private HashMap<String, VirtualQuery> virtualQueries;
    private HashMap<String, DrillResponseTextFormat> outputFormats;

    public DrillQueryManager(final File queryPath, final Map<String, String> templateParams) {
        this.queryPath = queryPath;
        this.templateParams = templateParams;
        this.responseCache = new HashMap<>();
        this.virtualQueries = new HashMap<>();

        this.outputFormats = new HashMap<>();
        // add the default output formats which can be overridden by user
        registerOutputFormat("default", new FormatResponseTableAsMarkdownTable());
        registerOutputFormat("table", new FormatResponseTableAsMarkdownTable());
        registerOutputFormat("list", new FormatResponseTableAsMarkdownList());
        registerOutputFormat("count", new FormatResponseTableAsMarkdownCountTable());

    }

    public DrillQueryResult perform(final String queryName) {

        if(responseCache.keySet().contains(queryName)){
            return responseCache.get(queryName);
        }

        File queryFile = new File(queryPath, queryName);
        String query="";

        DrillQueryResult result;

        if(queryFile.exists()) {
            try {
                query = new String(Files.readAllBytes(queryFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            result = processAsDrillQueryTemplate(query);
            DrillResponse payload = new Gson().fromJson(result.responseBody(), DrillResponse.class);

            result.setPayload(payload);

            cacheResponse(queryName, result);

            return result;

        }else{
            // assume virtual query
            // these are registered prior to processing template to allow custom formats and tables etc.
            if(virtualQueries.keySet().contains(queryName)){
                return virtualQueries.get(queryName).setQueryManager(this).execute();
            }
        }

        throw new RuntimeException("Could not find query or " + queryName);
    }

    private void cacheResponse(final String queryName, final DrillQueryResult result) {

        DrillQueryResult cacheResult = new DrillQueryResult();

        cacheResult.setPayload(result.payload());
        cacheResult.setNumberOfRows(result.numberOfRows());
        cacheResult.setMarkdown(outputFormats.get("default").format(result.payload()));
        cacheResult.setStatusCode(result.statusCode());
        cacheResult.setUrl(queryName);
        cacheResult.setResponseBody(result.responseBody());

        responseCache.put(queryName, cacheResult);

    }

    private DrillQueryResult processAsDrillQueryTemplate(final String query) {
        String bodyContent = fulfillAsTemplate(query, "{{ ", "  }}", templateParams);

        String url = "http://localhost:8047/query.json";

        DrillQueryResult result = new DrillQuery(bodyContent, url).post();

        if(result.statusCode()!=200){
            throw new RuntimeException(String.format("Error making request to %s %n %d %n %s", result.url(), result.statusCode(), result.responseBody()));
        }

        return result;
    }

    private String fulfillAsTemplate(final String body, final String startVariable, final String endVariable, final Map<String, String> params) {
        String retVal = body;
        for(String key : params.keySet()){
            retVal = retVal.replace(startVariable + key + endVariable, params.get(key));
        }
        return retVal;
    }

    public DrillQueryResult cacheVirtualQuery(final String nameOfQuery, final DrillResponse fakeResponse) {

        // not sure if I should return a cached item when we are tasked with caching it
        if(responseCache.keySet().contains(nameOfQuery)){
            System.out.println("**NOTE:** Returning cached virtual query");
            return responseCache.get(nameOfQuery);
        }

        DrillQueryResult result = new DrillQueryResult();

        result.setPayload(fakeResponse);
        result.setNumberOfRows(fakeResponse.rows.size());
        result.setMarkdown(outputFormats.get("default").format(fakeResponse));
        result.setStatusCode(200);
        result.setUrl(nameOfQuery);
        result.setResponseBody(new Gson().toJson(fakeResponse));

        cacheResponse(nameOfQuery, result);

        return result;
    }

    public DrillQueryManager registerVirtualQuery(final VirtualQuery virtualQuery) {
        virtualQueries.put(virtualQuery.getQueryName(), virtualQuery);
        return this;
    }

    public DrillQueryManager registerOutputFormat(final String name, final DrillResponseTextFormat outputFormat) {
        outputFormats.put(name, outputFormat);
        return this;
    }

    public DrillQueryResult perform(final String queryName, final ParsedMacro macro) {
        // use the macro to deal with any formatting etc.
        // at the moment I will do this 'after' the processing but it could be done in the processing itself

        final DrillQueryResult result = perform(queryName);

        if(macro.resultsAreLimited()){
            //we might be limiting globally so their might be no format
            // if that is the case then use default
            if(macro.formatType()==""){
                macro.setOutputFormat("default");
            }
        }

        if(macro.formatType()!=null && macro.formatType().length()>0){
            DrillResponseTextFormat formatter = outputFormats.get(macro.formatType());
            if(formatter==null){
                System.out.println("\n\n**ERROR** Unknown format " + macro.formatType());
            }else{
                System.out.println("Formatting with " + macro.formatType());
                if(macro.resultsAreLimited()){
                    DrillQueryResult limited = result.asLimitedResults(macro.limitResults());
                    result.setMarkdown(formatter.format(limited.payload()));
                    System.out.println(limited.markdown());

                }else {
                    result.setMarkdown(formatter.format(result.payload()));
                    System.out.println(result.markdown());
                }
            }

            // TODO: should I throw an error on unknown type?
        }

        return result;
    }
}
