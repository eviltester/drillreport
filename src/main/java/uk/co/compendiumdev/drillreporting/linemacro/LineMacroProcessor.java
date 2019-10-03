package uk.co.compendiumdev.drillreporting.linemacro;

public class LineMacroProcessor {
    private final String startMacroProcessing;
    private final String endMacroProcessing;

    public LineMacroProcessor(final String startMacroProcessing, final String endMacroProcessing) {
        this.startMacroProcessing = startMacroProcessing;
        this.endMacroProcessing = endMacroProcessing;
    }

    public ParsedMacro fromLine(final String line) {

        ParsedMacro retMacro = new ParsedMacro();

        if (line.trim().startsWith(startMacroProcessing) && line.trim().endsWith(endMacroProcessing)) {
            String macroDetails = line.trim();
            macroDetails = macroDetails.replace(startMacroProcessing, "");
            macroDetails = macroDetails.replace(endMacroProcessing, "");
            macroDetails = macroDetails.trim();

            final String[] macroPortions = macroDetails.split(" ");

            for(String macroPortion : macroPortions) {
                if (macroPortion.startsWith("QUERY:")) {
                    retMacro.setAsQuery();
                    retMacro.setQueryName(macroPortion.replace("QUERY:", "").trim());
                }
                if (macroPortion.startsWith("VQUERY:")) {
                    retMacro.setAsVirtualQuery();
                    retMacro.setQueryName(macroPortion.replace("VQUERY:", "").trim());
                }
                if (macroPortion.startsWith("METHOD:")) {
                    retMacro.setAsMethod();
                    retMacro.setMethodName(macroPortion.replace("METHOD:", "").trim());
                }
                if (macroPortion.startsWith("OUTPUTFORMAT:")){
                    retMacro.setOutputFormat(macroPortion.replace("OUTPUTFORMAT:", "").trim());
                }
                if(macroPortion.startsWith("LIMITROWS:")){
                    retMacro.setLimitResults(Integer.parseInt(macroPortion.replace("LIMITROWS:", "").trim()));
                }
            }

            if(retMacro.queryName().length()==0 && retMacro.methodName().length()==0){
                throw new RuntimeException("Could not find Query or Method name in macro line " + line);
            }

        }else{
            retMacro =  ParsedMacro.NOT_A_MACRO_LINE;
        }

        return retMacro;

    }
}
