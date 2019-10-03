package uk.co.compendiumdev.drillreporting.outputformats;

import uk.co.compendiumdev.drillreporting.api.DrillResponse;

import java.util.Map;

public class FormatResponseTableAsMarkdownTable implements DrillResponseTextFormat {

    public String format(final DrillResponse payload) {
        StringBuilder markdown = new StringBuilder();

        // header
        StringBuilder header = new StringBuilder();
        StringBuilder headerEnd = new StringBuilder();
        header.append("|");
        headerEnd.append("|");


        for(String columnName : payload.columns){
            header.append(" " + columnName + " |");
            headerEnd.append("---|");
        }
        header.append(String.format("%n"));
        headerEnd.append(String.format("%n"));


        markdown.append(header);
        markdown.append(headerEnd);

        for(Map<String,String> rowValues : payload.rows){
            StringBuilder row = new StringBuilder();
            row.append("|");
            for(String columnName : payload.columns){
                String rowValue = rowValues.get(columnName);
                if(rowValue==null){
                    rowValue = "";
                }
                row.append(" ");
                row.append(rowValue.replace("|",""));
                row.append(" |");
            }
            row.append(String.format("%n"));
            markdown.append(row);
        }

        return markdown.toString();
    }
}
