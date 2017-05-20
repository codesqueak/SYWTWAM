#!groovy

def checkoutCode() {
    stage('checkout')
            {
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/SYWTWAM.git']]]
            }
}

def build() {
    stage('build')
            {
                sh './gradlew clean build -x test'
            }
}

def test() {
    stage('test')
            {
                sh './gradlew test'
            }
}

def junitreport() {
    stage('JUnit report')
            {
                step([$class: 'JUnitResultArchiver', testResults: 'app/build/test-results/test/TEST-*.xml'])
            }
}

def findbugsreport() {
    stage('Findbugs report')
            {
                step([$class: 'FindBugsPublisher', pattern: 'app/build/reports/findbugs/main.xml'])
            }
}

def jacocoreport() {
    stage('Jacoco report')
            {
                step([$class: 'JacocoPublisher', execPattern: 'app/build/jacoco/jacocoTest.exec', pattern: 'app/build/jacoco/classpathdumps/com/codingrodent/**/*.class'])
            }
}

stage('execute So You Want To Write A Microservice build')
        {

            node {
                checkoutCode()
                build()
                test()
                junitreport()
                findbugsreport()
                jacocoreport()
            }
        }
