package uk.co.compendiumdev.drillreporting.outputformats;

import uk.co.compendiumdev.drillreporting.api.DrillResponse;

public class FormatResponseTableAsMarkdownCountTable implements DrillResponseTextFormat {

    public String format(final DrillResponse payload) {
        StringBuilder markdown = new StringBuilder();

        StringBuilder header = new StringBuilder();
        header.append("|Count|");
        header.append(String.format("%n"));

        header.append("|---|");
        header.append(String.format("%n"));

        markdown.append(header);

        int size = 0;
        if(payload!=null && payload.rows != null){
            size = payload.rows.size();
        }
        markdown.append("| " + size +  " |");
        markdown.append(String.format("%n"));
        markdown.append(String.format("%n"));

        return markdown.toString();
    }
}
