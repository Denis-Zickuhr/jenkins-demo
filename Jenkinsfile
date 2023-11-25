pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy to Homologation') {
            when {
                expression { env.BRANCH_NAME == 'dev' && buildStatus == 'SUCCESS' }
            }
            steps {
                sh 'git add .'
                sh 'git commit -m "Deploy to homologation"'
                sh 'git push origin homologation'
            }
        }
    }
}