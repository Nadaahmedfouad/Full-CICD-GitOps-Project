
def call() {
    stage('Build Node.js Application') {
        sh '''
            echo "Installing Node.js dependencies..."
            npm ci

            echo "Running application tests..."
            if npm run | grep -q '  test'; then
                npm test
            else
                echo "No test script configured. Skipping tests."
            fi

            echo "Node.js application build completed."
        '''
    }
}
