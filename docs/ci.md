# Continuous Integration

A number of Continuous Integration servers are in common use, but arguably the most popular is [Jenkins](https://jenkins.io/).

Jenkins provides hundreds of plugins to support building, deploying and automating any project. 

# Jenkins

While installing Jenkins is not an intrinsically complex task, it is best to follow the 
detailed [Guided Tour / Hello World](https://jenkins.io/doc/pipeline/tour/hello-world/) example from the documentation.
This will give a basic configuration which can be enhanced via plugins to do what we need.

Pay special attention to the use of a *Jenkinsfile* which is used to configure the pipeline steps.  This project utilizes a Jenkinsfile to make
configuration a simple task.

# Required Plugins

If you have installed Jenkins with the default set of plugins, several more may be required to execute the pipeline.  Make sure the following are installed:

## Findbugs

[FindBugs](http://findbugs.sourceforge.net/)

FindBugs is a program which uses static analysis to look for bugs in Java code.  The project uses this to identify possible problem area.  

[Plugin](https://wiki.jenkins-ci.org/display/JENKINS/FindBugs+Plugin)

This plugin generates the trend report for FindBugs, an open source program which uses static analysis to look for bugs in Java code.

## GitHub

[GitHub](https://github.com/)

If you don't know what this is, I'm curious why you are here...

[Plugin](https://wiki.jenkins-ci.org/display/JENKINS/GitHub+Plugin)

This plugin integrates Jenkins with Github projects

For test purposes, the Jenkinsfile *checkout* stage can remain pointed to this project as it functions correctly with read-only access.  Don't forget to
point to your own repository when you have created oned.

```checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/SYWTWAM.git']]]```

## Gradle

[Gradle](https://gradle.org/)

A build automation tool. But i'm sure you knew that already ?

[Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Gradle+Plugin)

This plugin makes it possible to invoke a Gradle build script as the main build step.

## JaCoCo

[JaCoCo](http://www.eclemma.org/jacoco)

JaCoCo is a code coverage library for Java. It is derived from [EclEmma](http://www.eclemma.org/). The project uses this to measure code coverage from the unit tests and
display the result

[Plugin](https://wiki.jenkins-ci.org/display/JENKINS/JaCoCo+Plugin)

This plugin allows you to capture code coverage report from JaCoCo. Jenkins will generate the trend report of coverage.



