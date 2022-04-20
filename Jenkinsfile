pipeline {
    agent {
        label 'android'
    }
    stages {
        stage('Build') {
            steps {
                bat './gradlew build clean'
                echo 'build for app passed'
            }
        }
        stage('Testing') {
            steps {
                bat './gradlew test'
                echo 'unit tests complete'
                bat './gradlew com.example.photo_gallery:androidTest'
                echo 'ran connected android tests'
                bat './gradlew espresso:androidTest'
                echo 'ran the espresso tests'
            }
        }
    }
}