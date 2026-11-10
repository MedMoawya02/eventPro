pipeline {
    agent any

    tools {
        // Utilise les noms définis dans la configuration globale
        maven 'Maven3'
        jdk 'jdk17'
    }

    stages {
        stage('Build & Test') {
            steps {
                // -B : mode batch (pas de couleurs interactives)
                sh 'mvn clean test -B'
            }
            post {
                always {
                    // Publie les résultats des tests JUnit dans Jenkins
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}