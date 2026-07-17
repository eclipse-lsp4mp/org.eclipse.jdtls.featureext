pipeline {
  agent any
  tools {
    jdk 'temurin-jdk21-latest'
  }
  environment {
    MAVEN_HOME = "$WORKSPACE/.m2/"
    MAVEN_USER_HOME = "$MAVEN_HOME"
  }
  stages {
    stage('Maven Build') {
      when {
        branch 'main'
      }
      steps {
        withMaven {
          sh './mvnw clean verify -B'
        }
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }
    stage('Deploy to downloads.eclipse.org') {
      when {
        branch 'main'
      }
      steps {
        sshagent(['projects-storage.eclipse.org-bot-ssh']) {
          sh '''
            VERSION=`grep -o '[0-9].*[0-9]' org.eclipse.jdtls.featureext.core/target/maven-archiver/pom.properties`
            targetDir=/home/data/httpd/download.eclipse.org/lsp4mp/jdtls-featureext/snapshots/$VERSION
            ssh genie.lsp4mp@projects-storage.eclipse.org rm -rf $targetDir
            ssh genie.lsp4mp@projects-storage.eclipse.org mkdir -p $targetDir
            scp -r org.eclipse.jdtls.featureext.site/target/*.zip genie.lsp4mp@projects-storage.eclipse.org:$targetDir
            ssh genie.lsp4mp@projects-storage.eclipse.org unzip $targetDir/*.zip -d $targetDir/repository
            '''
        }
      }
    }
  }
}
