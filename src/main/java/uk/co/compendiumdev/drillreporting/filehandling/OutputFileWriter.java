package uk.co.compendiumdev.drillreporting.filehandling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OutputFileWriter {
    private final String path;
    private final String filename;
    private boolean deleteIfExists;

    File outputFile;
    BufferedWriter outputWriter;

    public OutputFileWriter(final String path, final String filename) {

        this.path = path;
        this.filename = filename;
        this.deleteIfExists = true;
    }

    public OutputFileWriter setDeleteFileIfItExistsWhenOpening(boolean deleteit){
        this.deleteIfExists= deleteit;
        return this;
    }

    public void write(final String output) {

        ensureFileIsOpenForWriting();
        try {
            outputWriter.write(output);
        } catch (IOException e) {
            new RuntimeException("Could not write to file " + outputFile.getAbsolutePath(), e);
            e.printStackTrace();
        }
    }

    public void newLine() {
        ensureFileIsOpenForWriting();
        try {
            outputWriter.newLine();
        } catch (IOException e) {
            new RuntimeException("Could not write to file " + outputFile.getAbsolutePath(), e);
            e.printStackTrace();
        }
    }

    private void ensureFileIsOpenForWriting() {

        if(outputWriter!=null){
            return;
        }

        File outputPath = new File(path);
        outputPath.mkdirs();

        outputFile = new File(outputPath, filename);

        if(deleteIfExists) {
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }

        try {
            outputWriter = Files.newBufferedWriter(outputFile.toPath());
        } catch (IOException e) {
            new RuntimeException("Could not open file for writing " + outputFile.getAbsolutePath(), e);
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            outputWriter.close();
        } catch (IOException e) {
            new RuntimeException("Could not close file " + outputFile.getAbsolutePath(), e);
            e.printStackTrace();
        }
        outputWriter=null;
    }
}
