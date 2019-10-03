At the moment this is used as a relative system install via maven.

Split out into separate project to aid future development.

~~~~~~~~
        <repositories>
            <repository>
                <id>my-local-repo</id>
                <url>file:///Users/alanrichardson/Documents/GitHub/drillreport</url>
            </repository>
        </repositories>

<dependencies>
        <dependency>
            <groupId>uk.co.compendiumdev</groupId>
            <artifactId>drillreport</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
</dependencies>
~~~~~~~~