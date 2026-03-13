pipeline {
  agent {
    label 'docker'
  }

  environment {
    APP_NAME    = "democorp-employee-service"
    DOCKER_REG  = "local" // not used now
    DOCKER_REPO = "democorp-employee-service"
    IMAGE_TAG   = "${env.BUILD_NUMBER}"
    JAVA_HOME   = tool(name: 'JDK21', type: 'hudson.model.JDK')
    PATH        = "${JAVA_HOME}\\bin;${env.PATH}"
  }

  tools {
    git 'Default'
  }

  options {
    timestamps()
    ansiColor('xterm')
    buildDiscarder(logRotator(numToKeepStr: '20'))
    skipStagesAfterUnstable()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        bat 'mvnw.cmd -B -U clean verify'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Package') {
      steps {
        bat 'mvnw.cmd -B -DskipTests package'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('Docker Build') {
      steps {
        // Use a single line or ^ line break for Windows batch
        bat """
          docker build -t ${DOCKER_REPO}:${IMAGE_TAG} -t ${DOCKER_REPO}:latest .
        """
      }
    }

    // Optional push stage kept but disabled by default
    stage('Docker Push (optional)') {
      when { expression { return params?.PUSH_IMAGE == true } }
      environment {
        DOCKERHUB = credentials('docker-registry-creds') // create if you plan to push
      }
      steps {
        bat """
          echo ${DOCKERHUB_PSW} | docker login docker.io -u ${DOCKERHUB_USR} --password-stdin
          docker push ${DOCKER_REPO}:${IMAGE_TAG}
          docker push ${DOCKER_REPO}:latest
          docker logout
        """
      }
    }
  }

  parameters {
    booleanParam(name: 'PUSH_IMAGE', defaultValue: false, description: 'Push image to registry?')
  }

  post {
    success {
      echo "Build #${BUILD_NUMBER} succeeded. Image: ${DOCKER_REPO}:${IMAGE_TAG}"
    }
    failure {
      echo "Build failed. Check logs."
    }
    always {
      cleanWs(deleteDirs: true, notFailBuild: true)
    }
  }
}