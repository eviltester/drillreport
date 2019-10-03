package uk.co.compendiumdev.drillreporting.outputformats;

import uk.co.compendiumdev.drillreporting.api.DrillResponse;

import java.util.Map;

public class FormatResponseTableAsMarkdownList implements DrillResponseTextFormat {

    /*
        - first field
            - next field
            - next field
            - next field
     */
    @Override
    public String format(final DrillResponse payload) {
        StringBuilder markdown = new StringBuilder();

        for(Map<String,String> rowValues : payload.rows){
            StringBuilder row = new StringBuilder();
            String prefix = "- ";
            for(String columnName : payload.columns){

                row.append(prefix);

                String rowValue = rowValues.get(columnName);
                if(rowValue==null){
                    rowValue = "";
                }

                row.append(rowValue);
                row.append(String.format("%n"));

                prefix = "    - ";
            }
            row.append(String.format("%n"));
            markdown.append(row);
        }

        return markdown.toString();
    }
}
