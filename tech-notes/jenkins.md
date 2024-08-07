# Jenkins Tech Notes and Helpers
<a name="top"></a>

## Table of Contents
- [Sample Pipelines](#samplepipelines)
- [Code Snippets](#snippets)


<br/><br/>

---  
<br/>


<a name="samplepipelines"></a>
## Sample Pipelines

### Basic Pipeline Structure
```groovy
pipeline {
agent { node { label 'myAgent' } }

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

### Print Credentials in Console Log
```groovy
pipeline {
  agent { node { label 'myAgent' } }
  stages {
    stage('Dump credentials') {
      steps {
        script {
            def creds = ['dbPassword','vaultPassword','etc']
            for (item in creds) {
                withCredentials([usernamePassword(credentialsId: item, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                sh """
                echo '${item} : ${USERNAME} : ${PASSWORD}' >> creds.txt
                """
                }
            }
        }
      }
    }
    stage('Cat credentials') {
      steps {
        script {
            sh """
            cat creds.txt
            """
        }
      }
    }    
  }
}
```

[back to top](#top)

<br/><br/>

---  
<br/>

<a name="snippets"></a>
## Code Snippets

### Working with JSON
#### Sample JSON
```json
under construction
```
#### Parsing the Sample JSON
```groovy
under construction
```


[back to top](#top)
