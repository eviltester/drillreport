package uk.co.compendiumdev.drillreporting.virtualquery;

import uk.co.compendiumdev.drillreporting.api.DrillQueryManager;
import uk.co.compendiumdev.drillreporting.api.DrillQueryResult;

public interface VirtualQuery {

    String getQueryName();

    VirtualQuery setQueryManager(DrillQueryManager queryManager);

    DrillQueryResult execute();
}
