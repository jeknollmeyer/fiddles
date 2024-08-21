# Bash Snippets and Helpers
<a name="top"></a>
<description>


## Table of Contents
- [Code Snippets](#snippets)
  - [Sed](#sed)
  - [awk](#awk)
  - [jq](#jq)
  - [mail](#mail)
  - [case](#case)


<br/><br/>

---  
<br/>


<a name="snippets"></a>
## Code Snippets
Common bash examples and one-liners.


```bash
#### Line feed separater
#### sets explicit line feed. set when using "for line in..."
IFS=$'\n'

#### Scritp dir location
SCRIPT_DIR="$(cd "$(dirname "${0}")" && pwd)"

#### Logged in linux user
USERID=`whoami | awk '{print $1}'`


#### Show java system properties
${JAVA_HOME}/bin/java -XshowSettings:properties -version


#### Insert text at specific line in a file
#### insert at line 3
ex -s -c '3i|hello world' -c x file.txt


#### grep - Find only Ascii Characters
cat file.txt | grep --color='auto' -P "^ .*[[:ascii:]]:$"


#### sed - Remove Trailing Spaces from Lines
cat file.txt | 's/[[:space:]]*$//g' 


#### Bash/Linux - Change the Terminal Title
#### Add this to the `.bashrc` file:
# Set Terminal Window Title 
TERMINAL_TITLE="New-Title | ${HOSTNAME}"
PROMPT_COMMAND='echo -en "\033]0;${TERMINAL_TITLE}\a"'


#### openssl - Access cert file
openssl x509 -in JenkinsApache.crt -text


#### ldap search
#### Depends on oracle
export ORACLE_HOME=/oracle/product/v12.2.0.1_Client
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME/lib
wget -q https://.../repos/tree/General/archive-internal-release%2ldapsearch
ldapsearch -o ldif-wrap=no -x -LLL -h ldaps://stldc66.corp.host.test -p 636 -b dc=corp,dc=host,dc=test -D 'TEST\EID' -w '$TESTPW' -s sub '(name=EID)'



```

<a name="sed"></a>
### sed

```bash
#### sed - replace nth line
sed -i "${n}s/.*/replacement_text/" filename
```

<br/><br/>

---  
<br/>


<a name="awk"></a>
### awk

```bash
#### awk - print 3rd char in line
echo "your_line_here" | awk '{print substr($0, 3, 1)}'

#### awk - print 3rd to 8th chars in line
echo "your_line_here" | awk '{print substr($0, 3, 8)}'

```

[back to top](#top)

<br/><br/>

---  
<br/>


<a name="jq"></a>
### jq
> JSON parsing in bash/linux
> jq only prints to stdout. There is no inline substitution/replace. Redirect to update the input file.

**Sample File:**  
```json
{
  "Domain": "mercury",
  "LastUpdated": "",
  "ReleaseTrain": {
      "mercury-21.02": {
        "DeployEnv": {
           "itf"      : { "SchedStart":"2022-01-29", "SchedEnd":"2022-01-31", "CRQ":"CRQ000", "CRQStatus":"Draft", "ReleaseStatus":"NotStarted" },
           "mtf"      : { "SchedStart":"2022-01-29", "SchedEnd":"2022-01-31", "CRQ":"", "CRQStatus":"", "ReleaseStatus":"" },
           "prod"  : { "SchedStart":"", "SchedEnd":"", "CRQ":"", "CRQStatus":"", "ReleaseStatus":"" }
         }
      }
   }
}  
```

```bash
#### Use a variable to update a value in the file  
jq --arg NOW "$NOW" '.LastUpdated = $NOW' my.json > my.json.tmp && mv my.json.tmp my.json

#### Update a value many elements down
jq '.ReleaseTrain."mercury-21.02".DeployEnv.itf.CRQStatus = "COMPLETE"' my.json > my.json.tmp && mv my.json.tmp my.json


#### Use jq in Jenkins  
#### Get the binary from artifactory and set it in path within sh block  
curl -s --insecure --output "jq" "https://...../artifactory/archive-internal-stable/com/jq/jq-linux64"
mkdir bin
mv jq bin
chmod 775 bin/*
export PATH="`pwd`/bin:\${PATH}"
echo '{"fruit":{"name":"apple","color":"green","price":1.20}}' | jq '.'

#### Use jq with multiple vars
MYENV="itf"
RELEASE_TRAIN="mercury-21.0"
jq --arg E "$MYENV" --arg RELEASE "$RELEASE_TRAIN" '.RELEASE_TRAIN[$RELEASE][$E] += {SCHED_START: null, SCHED_END: null}' this.json


#### Query JSON and YAML with jq / yq
#### Select by value of a list item:
#### https://stackoverflow.com/questions/18592173/select-objects-based-on-value-of-variable-in-object-using-jq
cat org.yaml | yq e '.origins[].services[] | select(.name=="service")' - | head -20


#### Convert to JSON and use jq
cat org.yaml | yq e -j | jq --arg N "$NAME" '.origins[].services[] | select(.name==$N).repo_url'


#### YQ Install
#### https://github.com/mikefarah/yq/#install


#### JQ Install from Artifactory
JQ_PKG = "https://.../artifactory/archive-internal-stable/com/jq/jq-linux64"
mkdir bin
#### Download the linux jq package so we can parse JSON with shell/bash
curl -s --insecure --output "jq" "\${JQ_PKG}"
mv jq bin
chmod 775 bin/*
export PATH="`pwd`/bin:\${PATH}"
#### OR
curl -s --insecure --output "jq" "https://..../artifactory/archive-internal-stable/com/jq/jq-linux64" && chmod 777 jq; sudo mv jq /usr/bin/jq


#### Update List Item With Filtered Parent Object
#### https://unix.stackexchange.com/questions/680004/use-jq-to-update-property-of-object-that-contains-other-property-with-specific-v
cat ../../habitat-registry/registry/cera.yaml | yq e -j | jq '(."permission_groups"[] | select(.name == "Autobots"))."service_config"[]."service_groups" += [ "SOMEVALUE" ]' | grep "SOME"


#### Use jq with Vars
VAULT_URL=`cat sql-files/config/sql-config.json | jq --arg E "\${ENV}" --arg D "\${DATABASE}" '.DB_INFO.ENV[\$E].VAULT_URL' | sed 's/"//g'`
DB_USER=`cat sql-files/config/sql-config.json | jq --arg E "\${ENV}" --arg D "\${DATABASE}" '.DB_INFO.ENV[\$E].DATABASE[\$D].DB_USER' | sed 's/"//g'`
DB_HOST=`cat sql-files/config/sql-config.json | jq --arg E "\${ENV}" --arg D "\${DATABASE}" '.DB_INFO.ENV[\$E].DATABASE[\$D].DB_HOST' | sed 's/"//g'`
DB_SERV_NAME=`cat sql-files/config/sql-config.json | jq --arg E "\${ENV}" --arg D "\${DATABASE}" '.DB_INFO.ENV[\$E].DATABASE[\$D].DB_SERV_NAME' | sed 's/"//g'`


#### Merge 2 JSON Files:
#### https://e.printstacktrace.blog/merging-json-files-recursively-in-the-command-line/

#### JQ Delete item from a list
cat temp-archer.json | jq 'del(."alerts"."product".nonprod."localhost.company.int"[] | select( .  == "/tmp/app.jar"))'

#### jq - select parent items as list based on child object values
cat $REGISTRY | yq -o=json |  jq --arg N "dev1" --arg Y "domain/app" '.hosts[] | select(.services[].channel==$N and .services[].name==$Y).name


```

[back to top](#top)


<br/><br/>

---  
<br/>




<a name="mail"></a>
### mail
Enable mail on Linux
```bash
### Install sendmail   
sudo yum install sendmail sendmail-cf m4

###Edit sendmail.mc  
sudo vim /etc/mail/sendmail.mc

### replace line:  
dnl define(`SMART_HOST', `smtp.your.provider')dnl
### with:  
define(`SMART_HOST', `mailhost.mailhost.int:25')dnl

### Replace line:  
DAEMON_OPTIONS(`Port=smtp,Addr=127.0.0.1, Name=MTA')dnl
### with:  
dnl DAEMON_OPTIONS(`Port=smtp,Addr=127.0.0.1, Name=MTA')dnl

### regenerate config file   
sudo /etc/mail/make

### restart mail server  
sudo service sendmail restart

### send test email (update eID@org.com with your email)  
```bash
echo "Test 1 from $(hostname -f)"|mail -s "Test 1 $(hostname -f)" eID@org.com
``` 


[back to top](#top)

<br/><br/>

---  
<br/>


<a name="case"></a>
### case

```bash
case $1 in
-h)
  FUNC_USAGE
  FUNC_EXIT 0 ;;
-print)
  FUNC_PRINT ;;
-publish)
  FUNC_PUBLISH
  FUNC_PRINT_VERSION ;;
-secrets)
  FUNC_GET_SECRET_DATA "-keys" ;;
*)
  FUNC_USAGE
  MESSAGE -e "Invalid Parms. Exiting..."
  FUNC_EXIT 1 ;;
esac


### CASE STATEMENT (REMOVE SHIFT FOR SINGLE INPUT)
MESSAGE() {
case ${1} in
-i)
  LOG_LEVEL="INFO"
  shift ;;
-e)
  LOG_LEVEL="ERROR"
  shift ;;
*)
  LOG_LEVEL="INFO" ;;
esac
```

[back to top](#top)

<br/><br/>

---  
<br/>

