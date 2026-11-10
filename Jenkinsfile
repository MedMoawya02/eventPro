pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'jdk17'
    }

    stages {
        stage('Build & Test') {
            steps {
                sh 'mvn clean test -B'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java'
                    )
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests -B'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }

    post {
        failure {
            echo '❌ Build échoué. Vérifiez les logs.'
        }
        success {
            echo '✅ Build réussi. JAR disponible dans les artefacts.'
        }
    }
}