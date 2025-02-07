pipeline {
    agent any
    stages {
        stage('build') {
            steps {
                script {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'docker-compose up -d --build'
                }
            }
        }
    }
}
