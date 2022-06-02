package io.micronaut.build.util

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class VersionHandlingTest extends Specification {
    def "defaults to Gradle properties when looking for versions"() {
        def project = ProjectBuilder.builder().build()
        project.ext.micronautVersion = "1.0.0"
        project.ext.micronautDocsVersion = "1.5.0"

        when:
        def version = VersionHandling.versionOrDefault(project, alias)

        then:
        version == expectedVersion

        where:
        alias            | expectedVersion
        "micronaut"      | "1.0.0"
        "micronaut-docs" | "1.5.0"
        "unknown"        | ""
    }
}