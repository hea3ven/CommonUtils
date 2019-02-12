pipeline {
    agent {
        docker {
            image 'gradle:5.2-jdk8-alpine'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    tools {
        gradle "gradle"
    }

    stages {
        stage("Checkout") {
            steps {
                checkout scm
            }
        }
        stage("Release") {
            steps {
                withCredentials([
                        string(credentialsId: 'github_release_token', variable: 'github_release_token')]) {
                    script {
                        sh "if [ -e build/libs ]; then rm build/libs/*; fi"
                        def changes = ""
                        for (changeLog in currentBuild.changeSets) {
                            for(entry in changeLog.items) {
                                changes += "${entry.msg}\r\n".replace('\'', '\\\'')
                            }
                        }
                        sh "JAVA_OPTS=-Xmx1024m gradle clean build check publish githubRelease --stacktrace --info --no-daemon -PBUILD_NO=$BUILD_NUMBER -Pgithub_release_token=$github_release_token -Pchangelog='$changes' -Pmaven_url=file:///var/maven"
                    }
                }
            }
        }
    }
    
    post {
        success {
            archiveArtifacts 'build/libs/**/*.jar'
        }
    }
}
