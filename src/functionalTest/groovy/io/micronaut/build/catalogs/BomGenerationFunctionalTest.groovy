package io.micronaut.build.catalogs

import io.micronaut.build.AbstractFunctionalTest
import spock.lang.Issue

class BomGenerationFunctionalTest extends AbstractFunctionalTest {
    def "inlines Micronaut catalogs into the generated catalog"() {
        given:
        withSample("test-bom-module")

        when:
        run 'publishAllPublicationsToBuildRepository'

        then:
        def moduleDir = file("build/repo/io/micronaut/dummy/micronaut-test-bom-module/1.2.3")
        def catalogFile = new File(moduleDir, "micronaut-test-bom-module-1.2.3.toml")
        moduleDir.exists()
        catalogFile.exists()
        catalogFile.text.trim() == """
#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
dekorate = "1.0.3"
micronaut-aws = "3.1.1"
micronaut-test-bom-module = "1.2.3"

[libraries]
dekorate = {group = "io.dekorate", name = "dekorate-project", version.ref = "dekorate" }
micronaut-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-aws-alexa", version = "" }
micronaut-aws-alexa-httpserver = {group = "io.micronaut.aws", name = "micronaut-aws-alexa-httpserver", version = "" }
micronaut-aws-bom = {group = "io.micronaut.aws", name = "micronaut-aws-bom", version = "" }
micronaut-aws-common = {group = "io.micronaut.aws", name = "micronaut-aws-common", version = "" }
micronaut-aws-distributed-configuration = {group = "io.micronaut.aws", name = "micronaut-aws-distributed-configuration", version = "" }
micronaut-aws-parameter-store = {group = "io.micronaut.aws", name = "micronaut-aws-parameter-store", version = "" }
micronaut-aws-sdk-v1 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v1", version = "" }
micronaut-aws-sdk-v2 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v2", version = "" }
micronaut-aws-secretsmanager = {group = "io.micronaut.aws", name = "micronaut-aws-secretsmanager", version = "" }
micronaut-aws-service-discovery = {group = "io.micronaut.aws", name = "micronaut-aws-service-discovery", version = "" }
micronaut-function-aws = {group = "io.micronaut.aws", name = "micronaut-function-aws", version = "" }
micronaut-function-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-function-aws-alexa", version = "" }
micronaut-function-aws-api-proxy = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy", version = "" }
micronaut-function-aws-api-proxy-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy-test", version = "" }
micronaut-function-aws-custom-runtime = {group = "io.micronaut.aws", name = "micronaut-function-aws-custom-runtime", version = "" }
micronaut-function-aws-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-test", version = "" }
micronaut-function-client-aws = {group = "io.micronaut.aws", name = "micronaut-function-client-aws", version = "" }
micronaut-test-bom-module = {group = "io.micronaut.dummy", name = "micronaut-test-bom-module", version.ref = "micronaut-test-bom-module" }
""".trim()
    }

    def "can exclude an alias when inlining Micronaut catalogs into the generated catalog"() {
        given:
        withSample("test-bom-module")
        buildFile << """
        micronautBom {
            excludedInlinedAliases.add("micronaut-aws-common")
        }
        """
        when:
        run 'publishAllPublicationsToBuildRepository'

        then:
        def moduleDir = file("build/repo/io/micronaut/dummy/micronaut-test-bom-module/1.2.3")
        def catalogFile = new File(moduleDir, "micronaut-test-bom-module-1.2.3.toml")
        moduleDir.exists()
        catalogFile.exists()
        catalogFile.text.trim() == """
#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
dekorate = "1.0.3"
micronaut-aws = "3.1.1"
micronaut-test-bom-module = "1.2.3"

[libraries]
dekorate = {group = "io.dekorate", name = "dekorate-project", version.ref = "dekorate" }
micronaut-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-aws-alexa", version = "" }
micronaut-aws-alexa-httpserver = {group = "io.micronaut.aws", name = "micronaut-aws-alexa-httpserver", version = "" }
micronaut-aws-bom = {group = "io.micronaut.aws", name = "micronaut-aws-bom", version = "" }
micronaut-aws-distributed-configuration = {group = "io.micronaut.aws", name = "micronaut-aws-distributed-configuration", version = "" }
micronaut-aws-parameter-store = {group = "io.micronaut.aws", name = "micronaut-aws-parameter-store", version = "" }
micronaut-aws-sdk-v1 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v1", version = "" }
micronaut-aws-sdk-v2 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v2", version = "" }
micronaut-aws-secretsmanager = {group = "io.micronaut.aws", name = "micronaut-aws-secretsmanager", version = "" }
micronaut-aws-service-discovery = {group = "io.micronaut.aws", name = "micronaut-aws-service-discovery", version = "" }
micronaut-function-aws = {group = "io.micronaut.aws", name = "micronaut-function-aws", version = "" }
micronaut-function-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-function-aws-alexa", version = "" }
micronaut-function-aws-api-proxy = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy", version = "" }
micronaut-function-aws-api-proxy-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy-test", version = "" }
micronaut-function-aws-custom-runtime = {group = "io.micronaut.aws", name = "micronaut-function-aws-custom-runtime", version = "" }
micronaut-function-aws-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-test", version = "" }
micronaut-function-client-aws = {group = "io.micronaut.aws", name = "micronaut-function-client-aws", version = "" }
micronaut-test-bom-module = {group = "io.micronaut.dummy", name = "micronaut-test-bom-module", version.ref = "micronaut-test-bom-module" }
""".trim()
    }

    @Issue("https://github.com/micronaut-projects/micronaut-build/issues/284")
    def "uses a single version for all subprojects"() {
        withSample("multi-project-bom")
        settingsFile.text += """rootProject.name = '$rootName' """

        when:
        run 'publishAllPublicationsToBuildRepository'

        then:
        def moduleDir = file("build/repo/io/micronaut/freedom/micronaut-freedom-bom/1.2.3")
        def catalogFile = new File(moduleDir, "micronaut-freedom-bom-1.2.3.toml")
        moduleDir.exists()
        catalogFile.exists()
        println catalogFile.text
        catalogFile.text.trim() == """#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
micronaut-freedom = "1.2.3"

[libraries]
micronaut-freedom-bar = {group = "io.micronaut.freedom", name = "micronaut-freedom-bar", version.ref = "micronaut-freedom" }
micronaut-freedom-bom = {group = "io.micronaut.freedom", name = "micronaut-freedom-bom", version.ref = "micronaut-freedom" }
micronaut-freedom-foo = {group = "io.micronaut.freedom", name = "micronaut-freedom-foo", version.ref = "micronaut-freedom" }
""".trim()

        and:
        def pomFile = new File(moduleDir, "micronaut-freedom-bom-1.2.3.pom")
        pomFile.exists()
        println pomFile.text
        pomFile.text.contains """<properties>
    <micronaut.freedom.version>1.2.3</micronaut.freedom.version>
  </properties>"""
        pomFile.text.contains """<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>io.micronaut.freedom</groupId>
        <artifactId>micronaut-freedom-bar</artifactId>
        <version>\${micronaut.freedom.version}</version>
      </dependency>
      <dependency>
        <groupId>io.micronaut.freedom</groupId>
        <artifactId>micronaut-freedom-foo</artifactId>
        <version>\${micronaut.freedom.version}</version>
      </dependency>
    </dependencies>
  </dependencyManagement>"""

        where:
        rootName << ['freedom', 'freedom-parent']
    }

    @Issue("https://github.com/micronaut-projects/micronaut-build/issues/284")
    def "can explicitly define the main property name"() {
        withSample("multi-project-bom")
        settingsFile.text += """rootProject.name = '$rootName' """
        file("freedom-bom/build.gradle") << """
micronautBom {
   propertyName = 'democracy'
}
        """

        when:
        run 'publishAllPublicationsToBuildRepository'

        then:
        def moduleDir = file("build/repo/io/micronaut/freedom/micronaut-freedom-bom/1.2.3")
        def catalogFile = new File(moduleDir, "micronaut-freedom-bom-1.2.3.toml")
        moduleDir.exists()
        catalogFile.exists()
        println catalogFile.text
        catalogFile.text.trim() == """#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
micronaut-democracy = "1.2.3"

[libraries]
micronaut-freedom-bar = {group = "io.micronaut.freedom", name = "micronaut-freedom-bar", version.ref = "micronaut-democracy" }
micronaut-freedom-bom = {group = "io.micronaut.freedom", name = "micronaut-freedom-bom", version.ref = "micronaut-democracy" }
micronaut-freedom-foo = {group = "io.micronaut.freedom", name = "micronaut-freedom-foo", version.ref = "micronaut-democracy" }
""".trim()

        and:
        def pomFile = new File(moduleDir, "micronaut-freedom-bom-1.2.3.pom")
        pomFile.exists()
        println pomFile.text
        pomFile.text.contains """<properties>
    <micronaut.democracy.version>1.2.3</micronaut.democracy.version>
  </properties>"""
        pomFile.text.contains """<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>io.micronaut.freedom</groupId>
        <artifactId>micronaut-freedom-bar</artifactId>
        <version>\${micronaut.democracy.version}</version>
      </dependency>
      <dependency>
        <groupId>io.micronaut.freedom</groupId>
        <artifactId>micronaut-freedom-foo</artifactId>
        <version>\${micronaut.democracy.version}</version>
      </dependency>
    </dependencies>
  </dependencyManagement>"""

        where:
        rootName << ['freedom', 'freedom-parent']
    }
}