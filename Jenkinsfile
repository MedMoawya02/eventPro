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

        // ÉTAPE SONARQUBE
       stage('SonarQube Analysis') {
           steps {
               withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                   sh '''
                       mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar \
                       -Dsonar.projectKey=eventpro-api \
                       -Dsonar.host.url=http://host.docker.internal:9000 \
                       -Dsonar.login=$SONAR_TOKEN
                   '''
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
                        sourcePattern: 'src/main/java',
                        exclusionPattern: '**/dto/**,**/entity/**,**/mapper/**,**/repository/**,**/config/**,**/*Application*'
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