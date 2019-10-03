package uk.co.compendiumdev.drillreporting.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLExtractor {

    public Collection<String> extractUrlsFrom(final String description) {
        Set<String> extracted = new HashSet<>();

        String urlPattern = "(https?)://[^\\s$\"]*";
        final Pattern pattern = Pattern.compile(urlPattern);

        Matcher matcher = pattern.matcher(description);

        while (matcher.find()) {
            System.out.println(matcher.group());
            String theUrl = matcher.group();
            if (theUrl.endsWith(".")) {
                theUrl = theUrl.substring(0, theUrl.length() - 1);
            }
            extracted.add(theUrl);
        }

        return extracted;
    }
}
