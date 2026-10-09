
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean compile test-compile'
            }
        }

        stage('Run API Tests') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        always {
            // Publish standard TestNG results
            junit allowEmptyResults: true,
                  testResults: '**/target/surefire-reports/*.xml'

            // Publish ExtentReports HTML report
            publishHTML(target: [
                reportName: 'ExtentReports',
                reportDir: 'test-output',
                reportFiles: 'ExtentReport.html',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            // Archive report files
            archiveArtifacts(
                artifacts: 'test-output/**,target/surefire-reports/**',
                allowEmptyArchive: true
            )
        }

        success {
            echo 'API tests passed. Check ExtentReports for details.'
        }

        failure {
            echo 'API tests failed. Check the console and ExtentReports.'
        }
    }
}
