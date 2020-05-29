package io.micronaut.build

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.diagnostics.DependencyReportTask
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.tasks.Jar
import org.groovy.lang.groovydoc.tasks.GroovydocTask

/**
 * Micronaut internal Gradle plugin. Not intended to be used in user's projects.
 */
class MicronautBuildCommonPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.repositories.jcenter()
        project.version project.findProperty("projectVersion")
        project.extensions.create('micronautBuild', MicronautBuildExtension)
        configureJavaPlugin(project)
        configureDependencies(project)
        configureTasks(project)
        configureIdeaPlugin(project)
        configureCheckstyle(project)
        configureLicensePlugin(project)
        configureTestLoggerPlugin(project)
    }

    private void configureDependencies(Project project) {
        String micronautVersion = project.findProperty("micronautVersion")
        String groovyVersion = project.findProperty("groovyVersion")
        String spockVersion = project.findProperty("spockVersion")
        String micronautTestVersion = project.findProperty("micronautTestVersion")

        project.configurations {
            documentation
            all {
                resolutionStrategy.eachDependency { DependencyResolveDetails details ->
                    String group = details.requested.group
                    if(group == 'org.codehaus.groovy') {
                        details.useVersion(groovyVersion)
                    }
                }
            }
        }

        project.dependencies {
            annotationProcessor platform("io.micronaut:micronaut-bom:${micronautVersion}")
            implementation platform("io.micronaut:micronaut-bom:${micronautVersion}")
            testAnnotationProcessor platform("io.micronaut:micronaut-bom:${micronautVersion}")
            testImplementation platform("io.micronaut:micronaut-bom:${micronautVersion}")

            documentation "org.codehaus.groovy:groovy-templates:$groovyVersion"
            documentation "org.codehaus.groovy:groovy-dateutil:$groovyVersion"

            testImplementation("org.spockframework:spock-core:${spockVersion}") {
                exclude module:'groovy-all'
            }

            testImplementation "io.micronaut:micronaut-inject-groovy:${micronautVersion}"
            testImplementation "io.micronaut.test:micronaut-test-spock:$micronautTestVersion"
            testImplementation "cglib:cglib-nodep:3.3.0"
            testImplementation "org.objenesis:objenesis:3.1"

            testRuntimeOnly "ch.qos.logback:logback-classic:1.2.3"
            testImplementation "org.codehaus.groovy:groovy-test:$groovyVersion"
        }

        project.tasks.withType(GroovydocTask) {
            classpath += project.configurations.documentation
        }
    }

    private void configureJavaPlugin(Project project) {
        project.apply plugin:"groovy"
        project.apply plugin:"java-library"

        JavaPluginConvention convention = project.convention.getPlugin(JavaPluginConvention)
        MicronautBuildExtension micronautBuildExtension = project.extensions.getByType(MicronautBuildExtension)

        convention.with {
            sourceCompatibility = micronautBuildExtension.sourceCompatibility
            targetCompatibility = micronautBuildExtension.targetCompatibility
        }

        project.tasks.withType(Test) {
            jvmArgs '-Duser.country=US'
            jvmArgs '-Duser.language=en'

            reports.html.enabled = !System.getenv("GITHUB_ACTIONS")
            reports.junitXml.enabled = !System.getenv("GITHUB_ACTIONS")

        }

        project.tasks.withType(GroovyCompile) {
            groovyOptions.forkOptions.jvmArgs.add('-Dgroovy.parameters=true')
        }

        project.tasks.withType(JavaCompile){
            options.encoding = "UTF-8"
            options.compilerArgs.add('-parameters')
        }

        project.tasks.withType(Jar) {
            manifest {
                attributes('Automatic-Module-Name': "${project.group}.${project.name}".replaceAll('[^\\w\\.\\$_]', "_"))
                attributes('Implementation-Version': project.findProperty("projectVersion"))
                attributes('Implementation-Title': project.findProperty("title"))
            }
        }
    }

    void configureTasks(Project project) {
        project.tasks.register("allDeps", DependencyReportTask)
    }

    void configureIdeaPlugin(Project project) {
        project.with {
            apply plugin: 'idea'
            idea {
                module {
                    outputDir file('build/classes/java/main')
                    testOutputDir file('build/classes/groovy/test')
                }
            }
        }
    }

    void configureCheckstyle(Project project) {
        project.with {
            MicronautBuildExtension micronautBuildExtension = extensions.getByType(MicronautBuildExtension)
            apply plugin: 'checkstyle'
            checkstyle {
                configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
                toolVersion = micronautBuildExtension.checkstyleVersion

                // Per submodule
                maxErrors = 1
                maxWarnings = 10

                showViolations = true
            }
            checkstyleTest.enabled = false
        }
    }

    void configureLicensePlugin(Project project) {
        project.with {
            apply plugin: "com.diffplug.gradle.spotless"
            spotless {
                java {
                    licenseHeaderFile rootProject.file('config/spotless.license.java')
                    targetExclude 'src/test/**', 'build/generated-src/**'
                }
            }
        }
    }

    void configureTestLoggerPlugin(Project project) {
        project.with {
            apply plugin: "com.adarshr.test-logger"

            testlogger {
                theme 'standard-parallel'
                showFullStackTraces true
                showStandardStreams true
                showPassedStandardStreams false
                showSkippedStandardStreams false
                showFailedStandardStreams true
            }
        }
    }
}
