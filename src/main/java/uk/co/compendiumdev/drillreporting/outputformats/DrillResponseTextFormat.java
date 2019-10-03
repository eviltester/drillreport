package uk.co.compendiumdev.drillreporting.outputformats;

import uk.co.compendiumdev.drillreporting.api.DrillResponse;

public interface DrillResponseTextFormat {
    /*
            - first field
                - next field
                - next field
                - next field
         */
    String format(DrillResponse payload);
}
