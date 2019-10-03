package uk.co.compendiumdev.drillreporting.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO: create a DrillResponseBuilder for use in creating 'fake' responses in virtual queries rather than using public fields
public class DrillResponse {
    String queryId;
    public List<String> columns;
    public List<Map<String,String>> rows;
    String []metadata;
    String queryState;
    long attemptedAutoLimit;

    public DrillResponse withLimitedRows(final int limitResults) {
        DrillResponse limited = new DrillResponse();
        limited.queryId = queryId;
        limited.columns = columns;
        limited.metadata = metadata;
        limited.queryState = queryState;
        limited.attemptedAutoLimit = attemptedAutoLimit;
        limited.rows = new ArrayList<>();
        int limit = limitResults;
        for(Map<String,String> row : rows){
            if(limit<=0){
                return limited;
            }
            limited.rows.add(row);
            limit--;
        }
        return limited;
    }
}
