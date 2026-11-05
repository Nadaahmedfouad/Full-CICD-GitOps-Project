
def call(String deploymentFile, String imageName, String imageTag) {
    stage('Update GitOps Repository') {
        withCredentials([
            usernamePassword(
                credentialsId: 'github-credentials',
                usernameVariable: 'GIT_USER',
                passwordVariable: 'GIT_TOKEN'
            )
        ]) {
            withEnv([
                "DEPLOYMENT_FILE=${deploymentFile}",
                "IMAGE_NAME=${imageName}",
                "IMAGE_TAG=${imageTag}"
            ]) {
                sh '''
                    set -e

                    echo "Updating Kubernetes image tag..."

                    sed -i -E \
                      "s|image: ${IMAGE_NAME}:[^[:space:]]+|image: ${IMAGE_NAME}:${IMAGE_TAG}|" \
                      "$DEPLOYMENT_FILE"

                    git config user.name "Jenkins"
                    git config user.email "jenkins@localhost"

                    git add "$DEPLOYMENT_FILE"

                    if git diff --cached --quiet; then
                        echo "No deployment changes to commit."
                    else
                        git commit -m "Update iVolveApp image to ${IMAGE_TAG}"
                    fi

                    git -c credential.helper='!f() { echo "username=$GIT_USER"; echo "password=$GIT_TOKEN"; }; f' \
                        push origin HEAD:main
                '''
            }
        }
    }
}
