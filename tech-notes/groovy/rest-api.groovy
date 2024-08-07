import groovy.json.JsonSlurper
def jsonParser = new JsonSlurper()

def opts = [
    vault_url: "https://localhost:8300"
]

// Require "Command Line" Parameters and append them to opts list.
def argIndex = 0
while (argIndex < args.size()) {
    def key = args[argIndex++]
    switch(key) {
        case "-u":
        case "--username":
            opts.username = args[argIndex++]
            break
        case "-p":
        case "--password":
            opts.password = args[argIndex++]
            break
        default:
            println "Invalid parameter: $key"
            System.exit(1)
            break
    }
}


void clientToken(String vault_url, String username, String password) {
  // Post request with curl execute
  def approleLogin = new groovy.json.JsonBuilder([
    'role_id'  : username,
    'secret_id': password 
  ]).toPrettyString()
  //print approleLogin
  
  // Run curl with groovy
  def sout = new StringBuilder()
  def serr = new StringBuilder()
  def proc = [ 'bash', '-c', "curl -s --insecure --request POST --data '${approleLogin}' ${vault_url}/v1/auth/approle/login" ].execute()
  proc.consumeProcessOutput(sout, serr)
  proc.waitForOrKill(1000)
  def procout = new StringBuilder().append('\nSTDOUT: ').append(sout).append('\nSTDERR: ').append(serr)
  //println procout
  
  // try catch error output contains
  try { 
    assert !procout.toString().toLowerCase().contains("error") 
  } catch (Exception ex) {
    println "ERROR Invalid Login"
    System.exit(1)
  }
  
  def soutJson = new JsonSlurper().parseText(sout.toString())
  def client_token = soutJson."auth"."client_token"
  println client_token
}

clientToken("${opts.vault_url}", "${opts.username}", "${opts.password}")

