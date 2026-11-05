
def buildApp, buildAndPushImage, updateGitOpsRepo
pipeline {
    agent {
        label 'full-cicd-agent'
    }

    environment {
        IMAGE_NAME = 'nadafouuad/ivolve-app'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        DEPLOYMENT_FILE = 'kubernetes/deployment.yaml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Load Shared Library') {
            steps {
                script {
                    buildApp = load 'jenkins/shared-library/vars/buildApp.groovy'
                    buildAndPushImage = load 'jenkins/shared-library/vars/buildAndPushImage.groovy'
                    updateGitOpsRepo = load 'jenkins/shared-library/vars/updateGitOpsRepo.groovy'
                }
            }
        }
        stage('Build Application') {
            steps {
                dir('app') {
                    script {
                        buildApp()
                    }
                }
            }
        }

        stage('Build and Push Image') {
            steps {
                script {
                    buildAndPushImage(
                        env.IMAGE_NAME,
                        env.IMAGE_TAG
                    )
                }
            }
        }

        stage('Update GitOps') {
            steps {
                script {
                    updateGitOpsRepo(
                        env.DEPLOYMENT_FILE,
                        env.IMAGE_NAME,
                        env.IMAGE_TAG
                    )
                }
            }
        }
    }

    post {
        success {
            echo 'CI pipeline completed successfully.'
        }
        failure {
            echo 'CI pipeline failed. Check the logs.'
        }
        always {
            echo 'Pipeline execution finished.'
        }
    }
}
