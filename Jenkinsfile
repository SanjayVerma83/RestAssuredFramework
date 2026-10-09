
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven-3.9'
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
            // Publish TestNG results
            junit allowEmptyResults: true,
                  testResults: '**/target/surefire-reports/*.xml'

            // Publish ExtentReports HTML report
            publishHTML(target: [
                reportName: 'ExtentReports',
                reportDir: 'Reports',
                reportFiles: 'ExtentReport.html',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            // Archive test reports
            archiveArtifacts(
                artifacts: 'Reports/**,target/surefire-reports/**',
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
