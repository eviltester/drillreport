package uk.co.compendiumdev.drillreporting.filehandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TemplateFileReader {
    private final File template;
    BufferedReader reader;
    String line;

    public TemplateFileReader(final File inputTemplate) {
        this.template = inputTemplate;

        if(!inputTemplate.exists()){
            new RuntimeException(String.format("Template %s does not exist", inputTemplate.getAbsolutePath()));
        }
    }

    public boolean getNextLine(){

        try {

            if(reader==null) {
                reader = Files.newBufferedReader(template.toPath());
            }

            line = reader.readLine();

            if(line==null){
                return false;
            }

        } catch (IOException e) {
            new RuntimeException(String.format("Could not read template file %s", template.getAbsolutePath()), e);
            e.printStackTrace();
        }

        return true;
    }


    public String getCurrentLine() {
        return line;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            new RuntimeException(String.format("Could not close template file %s", template.getAbsolutePath()), e);
            e.printStackTrace();
        }
    }
}
