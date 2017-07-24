# Findbugs

FindBugs is an open source static code analyser for Java.  Plugins exists to integrate it into Eclipse, NetBeans, IntelliJ ,Gradle, Hudson, Maven, Bamboo and Jenkins.

In this project, it is used in the build process to detect potential issues and with a report process integrated into the Jenkins pipeline.

## Gradle

As part of the Java plugin, various tasks are added.  One of these, *check*, is used to perform  all verification tasks in the project.  The Findbugs plugin is a verifiction task
which automatically adds itself to the check task as a dependency.

To configure Findbugs, the following is added to the Gradle build file:

```groovy
findbugs {
    toolVersion = "3.0.1"
    sourceSets = [sourceSets.main]
    effort = "max"
    reportLevel = "high"
    findbugsTest.enabled = false
    ignoreFailures = true
}
```
This tells Findbugs to run against the project source, but not the test source. 

The output from the plugin can be customized. For example:

```groovy
tasks.withType(FindBugs) {
    reports {
        xml.enabled = true
        html.enabled = false
    }
}
```
This informs the plugin to only produce XML output, which in this instance, is used by report generation tools further down the tool chain. 

Full documentation is [here](https://docs.gradle.org/3.3/userguide/findbugs_plugin.html)

## Jenkins

To use Findbugs with Jenkins, the [Findbugs plugin](https://wiki.jenkins.io/display/JENKINS/FindBugs+Plugin) will need to be installed.

The Jenkins pipeline script `Jenkinsfile`, uses the output from the Findbugs plugin to generate a report.
```groovy
def findbugsreport() {
    stage('Findbugs report')
            {
                step([$class: 'FindBugsPublisher', pattern: 'app/build/reports/findbugs/main.xml'])
            }
}
```
This will generate a report per build, such as:

![alt text](images/findbugs.png "Findbugs Jenkins Report")

Various other reporting tools are also capable of processing the output from Findbugs, for example [Dashboard View](https://wiki.jenkins.io/display/JENKINS/Dashboard+View).
