package uk.co.compendiumdev.drillreporting.linemacro;

public class ParsedMacro {

    public static final ParsedMacro NOT_A_MACRO_LINE = new ParsedMacro();
    private String queryName="";
    private String queryType="";
    private String methodName="";
    private String formatType="";
    private int resultsLimit = -1;

    public void setQueryName(final String name) {
        this.queryName = name;
    }

    public void setAsQuery() {
        this.queryType = "QUERY";
    }

    public void setAsVirtualQuery() {
        this.queryType = "VQUERY";
    }

    public void setAsMethod() {
        this.queryType = "METHOD";
    }

    public void setMethodName(final String name) {
        this.methodName = name;
    }

    public void setOutputFormat(final String formatType) {
        this.formatType = formatType;
    }

    public String queryName() {
        return queryName;
    }

    public String methodName() {
        return methodName;
    }

    public String formatType() {
        return formatType;
    }

    public void setLimitResults(final int limit) {
        this.resultsLimit = limit;
    }

    public boolean resultsAreLimited(){
        return resultsLimit>=0;
    }

    public int limitResults() {
        return resultsLimit;
    }
}
