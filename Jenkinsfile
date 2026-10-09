pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven-3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git...'
                checkout scm
            }
        }

        stage('Build & Compile') {
            steps {
                echo 'Compiling test code and validating dependencies...'
                bat 'mvn -q compile test-compile'
            }
        }

        stage('Execute API Tests') {
            steps {
                echo 'Executing API Automation Suite...'
                bat 'mvn clean test -Denv=qa'
            }
        }
    }

    post {
        always {
            echo 'Checking generated report...'
            bat 'if exist Reports\\ExtentReport.html (echo Report found) else (echo Report NOT found)'

            echo 'Publishing ExtentReports...'
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'Reports',
                reportFiles: 'ExtentReport.html',
                reportName: 'ExtentReports'
            ])

            archiveArtifacts artifacts: 'Reports/**', allowEmptyArchive: true
        }

        success {
            echo 'All API tests executed successfully!'
        }

        failure {
            echo 'Pipeline failed during compilation or API test execution.'
        }
    }
}