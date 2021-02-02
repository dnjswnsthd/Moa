pipeline {
    agent none
    options { skipDefaultCheckout(true) }
    stages {
        stage('Build and Test') {
            agent {
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            options {skipDefaultCheckout(false)}
            steps {
                sh 'mvn -B -DskipTests -f ./Backend/pom.xml clean package'
            }
        }
        stage('Docker build') {
            agent any
            steps {
                sh 'docker build -t moafront:latest /var/jenkins_home/workspace/test/Frontend/moa/Dockerfile'
                sh 'docker build -t moaback:latest /var/jenkins_home/workspace/test/Backend/Dockerfile'
            }
        }
        stage('Docker run') {
            agent any
            steps {
                sh 'docker ps -f name=moafront -q \
                  | xargs --no-run-if-empty docker container stop'
                sh 'docker ps -f name=moaback -q \
                  | xargs --no-run-if-empty docker container stop'
                sh 'docker container ls -a -f name=moafront -q \
                  | xargs -r docker container rm'
                sh 'docker container ls -a -f name=moaback -q \
                  | xargs -r docker container rm'
                sh 'docker images -f dangling=true && \
                    docker rmi $(docker images -f "dangling=true" -q)'
                sh 'docker run -d --name moafront -p 80:80 moafront:latest'
                sh 'docker run -d --name moaback -p 8080:8080 moaback:latest'
            }
        }
    }
}