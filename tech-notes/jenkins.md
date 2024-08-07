# Jenkins Tech Notes and Helpers
<a name="top"></a>

## Table of Contents
[Sample Pipelines](samplepipelines)


<br/><br/>

---  
<br/>


<a name="bugs"></a>
## Sample Pipelines

### Basic Pipeline Structure
```groovy
pipeline {
agent { node { label 'DTL-LIN7' } }

parameters {
  string(name: "STRING_VAR", defaultValue: "", description: "Some Desc")
  choice(name: 'CHOICE_VAR', choices: '\nchoice1\nchoice2', description: 'Some Choice')
}
options {
  timeout(time: 1, unit: 'HOURS')
}
environment {
  SOME_VAR="somevar"
}
stages {
  stage('Stage1') {
    steps {
      script {
        withCredentials([usernamePassword(credentialsId: 'dummy-cred', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh """
            echo "test"
          """
        } //withCredentials
      } //script
    } //steps
  } //Stage1

  stage('Stage2') {
    steps {
      script {
        sh """
          echo "stage2"
        """
      } //script
    } //steps
  } //Stage2
} //stages
} //pipeline
```


[back to top](#top)
