package uk.co.compendiumdev.drillreporting;

import uk.co.compendiumdev.drillreporting.api.DrillQueryManager;
import uk.co.compendiumdev.drillreporting.api.DrillQueryResult;
import uk.co.compendiumdev.drillreporting.filehandling.OutputFileWriter;
import uk.co.compendiumdev.drillreporting.filehandling.TemplateFileReader;
import uk.co.compendiumdev.drillreporting.linemacro.LineMacroProcessor;
import uk.co.compendiumdev.drillreporting.linemacro.ParsedMacro;

import java.util.Map;

public class TemplatedInputReportGenerator {
    private final TemplateFileReader templateReader;
    private final OutputFileWriter outputWriter;
    private final Map<String, String> templateParams;
    private final DrillQueryManager queryManager;
    private int globalRowLimit;

    public TemplatedInputReportGenerator(final TemplateFileReader templateReader, final OutputFileWriter outputWriter,
                                         final Map<String, String> templateParams, final DrillQueryManager queryManager) {
        this.templateReader = templateReader;
        this.outputWriter = outputWriter;
        this.templateParams = templateParams;
        this.queryManager = queryManager;
        this.globalRowLimit=-1; // by default no limit
    }

    public void generate() {

        String startMacroProcessing = templateParams.get("_startMacroProcessing");
        String endMacroProcessing = templateParams.get("_endMacroProcessing");

        try {

            String line = "";

            while (templateReader.getNextLine()) {

                try {


                    line = templateReader.getCurrentLine();

                    final ParsedMacro macro = new LineMacroProcessor(startMacroProcessing, endMacroProcessing).fromLine(line);

                    if (macro == ParsedMacro.NOT_A_MACRO_LINE) {
                        outputWriter.write(line);
                        outputWriter.newLine();
                    } else {

                        // process the macro

                        if (macro.queryName().length() > 0) {

                            String queryName = macro.queryName();

                            System.out.println(String.format("Processing query %s", queryName));

                            if (globalRowLimit >= 0) {
                                // this might be a demo mode or test run
                                macro.setLimitResults(globalRowLimit);
                            }

                            DrillQueryResult response = queryManager.perform(queryName, macro);

                            if (response.statusCode() == 200) {
                                outputWriter.write(response.markdown());
                                outputWriter.newLine();
                            } else {
                                System.out.println(String.format("ERROR: Processing query %s", queryName));
                                System.out.println(response.responseBody());
                            }
                        }

                        // this should be a registered list of commands that map to method names
                        if (macro.methodName().length() > 0) {
                            //                    if (methodName.contentEquals("getUrlsListWith404s")) {
                            //                        String methodOutput = getUrlsListWith404s(templatePath);
                            //                        outputWriter.write(methodOutput);
                            //                        outputWriter.newLine();
                            //                    }
                        }

                    }
                }catch(Exception e){
                    System.out.println("Error generating report " + e.getMessage());
                    e.printStackTrace();
                    System.out.println("BUT CARRYING ON REGARDLESS");
                }
            }

        }catch(Exception e){
            System.out.println("Error generating report " + e.getMessage());
            e.printStackTrace();
        }
    }

    public TemplatedInputReportGenerator limitResults(final int globalRowLimit) {
        this.globalRowLimit = globalRowLimit;
        return this;
    }
}
