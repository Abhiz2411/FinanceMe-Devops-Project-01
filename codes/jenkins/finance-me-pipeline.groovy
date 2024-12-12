pipeline {
    agent { label 'slave' }
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhublogincred')
    }

    stages {
        stage('SCM_checkout') {
            steps {
                echo '---- Perform SCM checkout ----'
                git 'https://github.com/Abhiz2411/finance-me-banking-finance.git'
                echo '---- SCM Checkout Done ----'
            }
        }
        stage('Application_Build') {
            steps {
                echo '---- Perform application build ----'
                sh 'mvn clean package'
                echo '---- Application build complete ----'
            }
        }
        stage('Docker build') {
            steps {
                echo '---- Perform Docker build ----'
                sh 'docker build -t abhiz2411/bankapp1:${BUILD_NUMBER} .'
                sh 'docker tag abhiz2411/bankapp1:${BUILD_NUMBER} abhiz2411/bankapp1:latest'
                sh 'docker images'
                echo '---- Docker build complete ----'
            }
        }
        stage('DockerHub login') {
            steps {
                echo '---- Login to DockerHub ----'
                sh 'echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin'
                echo '---- Logged in to DockerHub ----'
            }
        }
        stage('Publish image to DockerHub') {
            steps {
                echo '---- Push Docker images to DockerHub ----'
                sh 'docker push abhiz2411/bankapp1:latest'
                echo '---- Imaged Pushed to DockerHub ----'
            }
        }
        stage('Deploy application to production server') {
            steps {
                echo '---- Deploying the application ----'
                ansiblePlaybook become: true, credentialsId: 'slaveuser', disableHostKeyChecking: true, installation: 'ansible', inventory: '/etc/ansible/hosts', playbook: 'ansible-playbook.yml', sudoUser: null, vaultTmpPath: ''
                echo '---- Application Deployed ----'
            }
        }
        stage('Docker image cleaning'){
            steps{
                echo '---- Cleaning Docker images to save space -----'
                sh 'docker rmi abhiz2411/bankapp1 abhiz2411/bankapp1:${BUILD_NUMBER}'
                sh 'docker images'
                echo '------ Cleaned Docker images -------'
            }
        }
    }
    post{
        failure{
          sh "echo 'Send mail on failure'"
			mail bcc: 'abhijitzende75@gmail.com', body: 'Jenkins-${JOB_NAME}-${BUILD_NUMBER} status', cc: 'abhijitzende75@gmail.com', from: '', replyTo: '', subject: 'App Deployment Failed ', to: 'abhijitzende75@gmail.com'
        }
        success{
          sh "echo 'Send mail on Successful'"
			mail bcc: 'abhijitzende75@gmail.com', body: "jenkins-${JOB_NAME}-${BUILD_NUMBER} status", cc: 'abhijitzende75@gmail.com', from: '', replyTo: '', subject: 'App Deployment Successful ', to: 'abhijitzende75@gmail.com'
        }
    }
}