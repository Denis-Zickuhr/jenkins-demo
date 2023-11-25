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
                script {
                    def mvnOutput = bat(script: 'mvn clean install', returnStatus: true)

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

//         stage('Deploy to Homologation') {
//             when {
//                 expression { env.BRANCH_NAME == 'dev' && env.BUILD_STATUS == 'SUCCESS' }
//             }
//             steps {
//                 bat 'git add .'
//                 bat 'git commit -m "Deploy to homologation"'
//                 bat 'git push origin homologation'
//             }
//         }
    }
}