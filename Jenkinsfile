pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test') {
            steps {
                script {
                    def mvnOutput = bat(script: 'mvn test', returnStatus: true)

                    if (mvnOutput == 0) {
                        echo "Build status: SUCCESS"
                        currentBuild.result = 'SUCCESS'
                    } else {
                        echo "Build status: FAILURE"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }

        stage('Deploy to Homologation') {
            when {
                expression { env.BRANCH_NAME == 'dev' && currentBuild.result == 'SUCCESS' }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    script {
                        def REPO_URL = "https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Denis-Zickuhr/jenkins-demo.git"
                        bat 'git config --global credential.helper "store --file=.git-credentials"'
                        bat 'git config --global user.email "coringatheboss@gmail.com"'
                        bat 'git config --global user.name "${GIT_USERNAME}"'
                        bat 'git checkout homologation'
                        bat 'git merge origin/dev'
                        bat "git push ${REPO_URL}"
                    }
                }
            }
        }
    }
}