pipeline {
    agent any
    environment {
        IMAGE_REPOSITORY = 'reg.docker.dsyun.io'
        IMAGE_NAME = 'xfschain/xfschain-explorer-java'
     }
    options {
        gitLabConnection('gitlab')
    }
    stages {
        stage('Test'){
            when {
                not { branch 'main' }
            }
            steps {
                updateGitlabCommitStatus name: 'Test', state: 'pending'
                sh 'mvn test'
            }
            post {
                success {
                    updateGitlabCommitStatus name: 'Test', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: 'Test', state: 'failed'
                }
            }
        }
        stage('Build') {
            when {
                branch 'develop'
            }
            steps {
                updateGitlabCommitStatus name: 'Build', state: 'pending'
                sh 'mvn -Dspring.profiles.active=prod -Dmaven.test.skip=true package'
            }
            post {
                success {
                    updateGitlabCommitStatus name: 'Build', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: 'Build', state: 'failed'
                }
            }
        }
        stage('Release'){
            when {
                branch 'develop'
            }
            steps {
                script {
                    updateGitlabCommitStatus name: 'Release', state: 'pending'
                }
            }
            post {
                success {
                    updateGitlabCommitStatus name: 'Release', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: 'Release', state: 'failed'
                }
            }
        }
    }
}