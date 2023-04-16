def JOB_NAME_PARTS = JOB_NAME.tokenize('/') as String[]
def PROJECT_GIT = JOB_NAME_PARTS[JOB_NAME_PARTS.length - 2]
def REPOSITORY_GIT = JOB_NAME_PARTS[JOB_NAME_PARTS.length - 1]
def APPLICATION_NAME = ''
def APPLICATION_VERSION = ''
def APPLICATION_BUILD = ''
def CONTAINER_REGISTRY_SERVER = 'harbor.c2.dav.fr'
def CONTAINER_REGISTRY_PROJECT = ''
def CONTAINER_IMAGE = ''
def NAMESPACE_TBS = 'wrk-dev'
def MAVEN_CMD = 'JAVA_HOME=/opt/jdk-17 /opt/maven/bin/mvn'

pipeline {
  agent {
    node {
      label 'ULA_DOCKER03'
    }
  }

  parameters {
    string(name: 'projectGIT', description: 'Git Project Name', defaultValue: "${PROJECT_GIT}")
    string(name: 'repositoryGIT', description: 'Git Repository Name GIT', defaultValue: "${REPOSITORY_GIT}")
    gitParameter branchFilter: 'origin/(.*)', defaultValue: 'master', name: 'branchGIT', type: 'PT_BRANCH'
  }

  stages {
    stage ('Checkout SCM') {
      steps {
        script {
          cleanWs()
          checkout scm

          APPLICATION_NAME = readMavenPom().getArtifactId()
          APPLICATION_VERSION = readMavenPom().getVersion()
          APPLICATION_BUILD = "${env.BUILD_ID}"
          if ("${branchGIT}" == 'develop') { // vs env.BRANCH_NAME
            APPLICATION_VERSION = "${APPLICATION_VERSION}" + '.' + "${APPLICATION_BUILD}"
          }
          CONTAINER_REGISTRY_PROJECT = "${projectGIT.toLowerCase()}"
          CONTAINER_IMAGE = "${CONTAINER_REGISTRY_PROJECT}/${APPLICATION_NAME}:${APPLICATION_VERSION}"

          echo """
                ----------------- Soft Parameters Values -----------------
                GIT
                Project   : $projectGIT
                Repository: $repositoryGIT
                Branch    : $branchGIT

                Application
                Name   : $APPLICATION_NAME
                Version: $APPLICATION_VERSION

                Container Registry
                Server     : $CONTAINER_REGISTRY_SERVER
                Project    : $CONTAINER_REGISTRY_PROJECT
                Repository : $CONTAINER_REGISTRY_PROJECT/$APPLICATION_NAME
                Tag/Version: $APPLICATION_VERSION
                Image      : $CONTAINER_IMAGE
                ----------------------------------------------------------
               """

          sh("id; java -version; ${MAVEN_CMD} -version")
        }
      }
    }

    stage ("Static Code Analysis") {
      steps {
        echo "Static Code Analysis: $repositoryGIT"
        //sh "${MAVEN_CMD} checkstyle:checkstyle -s maven-settings.xml"
      }
    }

    stage("Build Source + Code Coverage/Quality") {
      steps {
        script {
          echo "Build Source: $repositoryGIT"
          sh "${MAVEN_CMD} clean package -DskipTests -s maven-settings.xml" //  -U sonar:sonar
        }
      }
    }
    // withMaven(jdk: 'java-17-openjdk', maven:'Maven') {
    //   sh "${MAVEN_CMD} clean deploy -gs ${MAVEN_SETTINGS} -Dmaven.repo.local=${MAVEN_REPOSITORY}/ -Dsonar.login=${SONAR_LOGIN} -U sonar:sonar"
    // }

    stage ("Build Image") {
      steps {
        script {
          echo("Build Iamge: $repositoryGIT")
          sh "docker build -t $CONTAINER_IMAGE ."
        }
      }
    }

    stage ("Tag & Push Image to Container Registry") {
      steps {
        script {
          sh (label: "Tag image to container registry", returnStatus: true, script: "docker tag ${CONTAINER_IMAGE} ${CONTAINER_REGISTRY_SERVER}/${CONTAINER_IMAGE}")
          withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'Harbor', usernameVariable: 'username', passwordVariable: 'password']]) {
            sh "docker login -u ${env.username} -p ${env.password} ${CONTAINER_REGISTRY_SERVER}"
          }
          sh (label: "Push image to container registry", returnStatus: true, script: "docker push ${CONTAINER_REGISTRY_SERVER}/${CONTAINER_IMAGE}")
          sh (label: "Remove local image container", returnStatus: true, script: "docker rmi -f ${CONTAINER_IMAGE} ${CONTAINER_REGISTRY_SERVER}/${CONTAINER_IMAGE}")
        }
      }
    }

    // stage ("kp Build Image") {
    //   steps {
    //     script {
    //       echo("Build Iamge: $repositoryGIT")
    //       //withCredentials([usernamePassword(credentialsId: 'svtbs', passwordVariable: 'password', usernameVariable: 'username')]) {
    //       withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'ad_scdsiml', usernameVariable: 'username', passwordVariable: 'password']]) {            
    //         sh "tkgi get-kubeconfig tkgi-c2-nonprod -a tkgi.c2.dav.fr -u ${env.username} -p ${env.password}"
    //         //sh "tkgi get-kubeconfig tkgi-c2-nonprod -a tkgi.c2.dav.fr -u 'svtbs' -p 'FWX6db+SCN'"
    //         //sh "tkgi get-kubeconfig tkgi-c2-nonprod -a tkgi.c2.dav.fr -u 'scdsiml' -p 'xxx'"
    //         sh "docker login -u ${env.username} -p ${env.password} ${CONTAINER_REGISTRY_SERVER}"
    //         sh "kp image delete $APPLICATION_NAME-${APPLICATION_VERSION} --namespace ${NAMESPACE_TBS} || true"
    //         sh "echo 'Waiting for image deletion' && sleep 5"
    //         sh "kp image create ${APPLICATION_NAME}-${APPLICATION_VERSION} \
    //             --namespace ${NAMESPACE_TBS} \
    //             --tag ${CONTAINER_REGISTRY_SERVER}/${CONTAINER_IMAGE} \
    //             --local-path . \
    //             --env BP_MAVEN_BUILD_ARGUMENTS=\"-Dmaven.test.skip=true validate -gs maven-settings.xml\" \
    //             --wait"
    //         sh "test \$(kp build status ${APPLICATION_NAME}-${APPLICATION_VERSION} --namespace ${NAMESPACE_TBS} | grep Status | awk '{print \$2}') == 'SUCCESS'"
    //       }
    //     }
    //   }
    // }
    // --env JAVA_TOOL_OPTIONS="-Xmx512m" \

    stage ('Tests') {
      steps {
        echo 'UT & IT'
      }
    }
  }
}
