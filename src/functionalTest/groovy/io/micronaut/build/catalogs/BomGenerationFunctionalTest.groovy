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
        catalogFile.text.trim() == """#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
alexa-ask-sdk = "2.71.0"
aws-java-sdk-v1 = "1.12.505"
aws-java-sdk-v2 = "2.20.102"
aws-lambda = "1.2.2"
aws-lambda-events = "3.11.2"
aws-lambda-java-serialization = "1.1.2"
dekorate = "3.7.0"
junit = "5.9.10"
micronaut-aws = "4.0.1"
micronaut-test-bom-module = "1.2.3"

[libraries]
alexa-ask-sdk = {group = "com.amazon.alexa", name = "ask-sdk", version.ref = "alexa-ask-sdk" }
alexa-ask-sdk-core = {group = "com.amazon.alexa", name = "ask-sdk-core", version.ref = "alexa-ask-sdk" }
alexa-ask-sdk-lambda = {group = "com.amazon.alexa", name = "ask-sdk-lambda-support", version.ref = "alexa-ask-sdk" }
aws-java-sdk-core = {group = "com.amazonaws", name = "aws-java-sdk-core", version.ref = "aws-java-sdk-v1" }
aws-lambda-core = {group = "com.amazonaws", name = "aws-lambda-java-core", version.ref = "aws-lambda" }
aws-lambda-events = {group = "com.amazonaws", name = "aws-lambda-java-events", version.ref = "aws-lambda-events" }
aws-lambda-java-serialization = {group = "com.amazonaws", name = "aws-lambda-java-serialization", version.ref = "aws-lambda-java-serialization" }
awssdk-secretsmanager = {group = "software.amazon.awssdk", name = "secretsmanager", version.ref = "aws-java-sdk-v2" }
boms-junit = {group = "org.junit", name = "junit-bom", version.ref = "junit" }
dekorate = {group = "io.dekorate", name = "dekorate-project", version.ref = "dekorate" }
micronaut-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-aws-alexa", version.ref = "micronaut-aws" }
micronaut-aws-alexa-httpserver = {group = "io.micronaut.aws", name = "micronaut-aws-alexa-httpserver", version.ref = "micronaut-aws" }
micronaut-aws-apigateway = {group = "io.micronaut.aws", name = "micronaut-aws-apigateway", version.ref = "micronaut-aws" }
micronaut-aws-bom = {group = "io.micronaut.aws", name = "micronaut-aws-bom", version.ref = "micronaut-aws" }
micronaut-aws-cloudwatch-logging = {group = "io.micronaut.aws", name = "micronaut-aws-cloudwatch-logging", version.ref = "micronaut-aws" }
micronaut-aws-common = {group = "io.micronaut.aws", name = "micronaut-aws-common", version.ref = "micronaut-aws" }
micronaut-aws-distributed-configuration = {group = "io.micronaut.aws", name = "micronaut-aws-distributed-configuration", version.ref = "micronaut-aws" }
micronaut-aws-lambda-events-serde = {group = "io.micronaut.aws", name = "micronaut-aws-lambda-events-serde", version.ref = "micronaut-aws" }
micronaut-aws-parameter-store = {group = "io.micronaut.aws", name = "micronaut-aws-parameter-store", version.ref = "micronaut-aws" }
micronaut-aws-sdk-v1 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v1", version.ref = "micronaut-aws" }
micronaut-aws-sdk-v2 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v2", version.ref = "micronaut-aws" }
micronaut-aws-secretsmanager = {group = "io.micronaut.aws", name = "micronaut-aws-secretsmanager", version.ref = "micronaut-aws" }
micronaut-aws-service-discovery = {group = "io.micronaut.aws", name = "micronaut-aws-service-discovery", version.ref = "micronaut-aws" }
micronaut-aws-ua = {group = "io.micronaut.aws", name = "micronaut-aws-ua", version.ref = "micronaut-aws" }
micronaut-function-aws = {group = "io.micronaut.aws", name = "micronaut-function-aws", version.ref = "micronaut-aws" }
micronaut-function-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-function-aws-alexa", version.ref = "micronaut-aws" }
micronaut-function-aws-api-proxy = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy", version.ref = "micronaut-aws" }
micronaut-function-aws-api-proxy-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy-test", version.ref = "micronaut-aws" }
micronaut-function-aws-custom-runtime = {group = "io.micronaut.aws", name = "micronaut-function-aws-custom-runtime", version.ref = "micronaut-aws" }
micronaut-function-aws-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-test", version.ref = "micronaut-aws" }
micronaut-function-client-aws = {group = "io.micronaut.aws", name = "micronaut-function-client-aws", version.ref = "micronaut-aws" }
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
        catalogFile.text.trim() == """#
# This file has been generated by Gradle and is intended to be consumed by Gradle
#
[metadata]
format.version = "1.1"

[versions]
alexa-ask-sdk = "2.71.0"
aws-java-sdk-v1 = "1.12.505"
aws-java-sdk-v2 = "2.20.102"
aws-lambda = "1.2.2"
aws-lambda-events = "3.11.2"
aws-lambda-java-serialization = "1.1.2"
dekorate = "3.7.0"
junit = "5.9.10"
micronaut-aws = "4.0.1"
micronaut-test-bom-module = "1.2.3"

[libraries]
alexa-ask-sdk = {group = "com.amazon.alexa", name = "ask-sdk", version.ref = "alexa-ask-sdk" }
alexa-ask-sdk-core = {group = "com.amazon.alexa", name = "ask-sdk-core", version.ref = "alexa-ask-sdk" }
alexa-ask-sdk-lambda = {group = "com.amazon.alexa", name = "ask-sdk-lambda-support", version.ref = "alexa-ask-sdk" }
aws-java-sdk-core = {group = "com.amazonaws", name = "aws-java-sdk-core", version.ref = "aws-java-sdk-v1" }
aws-lambda-core = {group = "com.amazonaws", name = "aws-lambda-java-core", version.ref = "aws-lambda" }
aws-lambda-events = {group = "com.amazonaws", name = "aws-lambda-java-events", version.ref = "aws-lambda-events" }
aws-lambda-java-serialization = {group = "com.amazonaws", name = "aws-lambda-java-serialization", version.ref = "aws-lambda-java-serialization" }
awssdk-secretsmanager = {group = "software.amazon.awssdk", name = "secretsmanager", version.ref = "aws-java-sdk-v2" }
boms-junit = {group = "org.junit", name = "junit-bom", version.ref = "junit" }
dekorate = {group = "io.dekorate", name = "dekorate-project", version.ref = "dekorate" }
micronaut-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-aws-alexa", version.ref = "micronaut-aws" }
micronaut-aws-alexa-httpserver = {group = "io.micronaut.aws", name = "micronaut-aws-alexa-httpserver", version.ref = "micronaut-aws" }
micronaut-aws-apigateway = {group = "io.micronaut.aws", name = "micronaut-aws-apigateway", version.ref = "micronaut-aws" }
micronaut-aws-bom = {group = "io.micronaut.aws", name = "micronaut-aws-bom", version.ref = "micronaut-aws" }
micronaut-aws-cloudwatch-logging = {group = "io.micronaut.aws", name = "micronaut-aws-cloudwatch-logging", version.ref = "micronaut-aws" }
micronaut-aws-distributed-configuration = {group = "io.micronaut.aws", name = "micronaut-aws-distributed-configuration", version.ref = "micronaut-aws" }
micronaut-aws-lambda-events-serde = {group = "io.micronaut.aws", name = "micronaut-aws-lambda-events-serde", version.ref = "micronaut-aws" }
micronaut-aws-parameter-store = {group = "io.micronaut.aws", name = "micronaut-aws-parameter-store", version.ref = "micronaut-aws" }
micronaut-aws-sdk-v1 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v1", version.ref = "micronaut-aws" }
micronaut-aws-sdk-v2 = {group = "io.micronaut.aws", name = "micronaut-aws-sdk-v2", version.ref = "micronaut-aws" }
micronaut-aws-secretsmanager = {group = "io.micronaut.aws", name = "micronaut-aws-secretsmanager", version.ref = "micronaut-aws" }
micronaut-aws-service-discovery = {group = "io.micronaut.aws", name = "micronaut-aws-service-discovery", version.ref = "micronaut-aws" }
micronaut-aws-ua = {group = "io.micronaut.aws", name = "micronaut-aws-ua", version.ref = "micronaut-aws" }
micronaut-function-aws = {group = "io.micronaut.aws", name = "micronaut-function-aws", version.ref = "micronaut-aws" }
micronaut-function-aws-alexa = {group = "io.micronaut.aws", name = "micronaut-function-aws-alexa", version.ref = "micronaut-aws" }
micronaut-function-aws-api-proxy = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy", version.ref = "micronaut-aws" }
micronaut-function-aws-api-proxy-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-api-proxy-test", version.ref = "micronaut-aws" }
micronaut-function-aws-custom-runtime = {group = "io.micronaut.aws", name = "micronaut-function-aws-custom-runtime", version.ref = "micronaut-aws" }
micronaut-function-aws-test = {group = "io.micronaut.aws", name = "micronaut-function-aws-test", version.ref = "micronaut-aws" }
micronaut-function-client-aws = {group = "io.micronaut.aws", name = "micronaut-function-client-aws", version.ref = "micronaut-aws" }
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
