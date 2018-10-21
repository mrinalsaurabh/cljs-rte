# cljs-rte

A [re-frame](https://github.com/Day8/re-frame) application designed to provide common react components to other applications.

***Deployment***
**Pre-Requisites:**
* Maven - https://tecadmin.net/install-apache-maven-on-centos/
* Lein
* Aws-cli - http://docs.aws.amazon.com/cli/latest/userguide/installing.html

**Steps to Deploy:**
* Checkout git@git.thoughtworks.net:operational-insights/cljs-rte.git
* Make changes, checkin.
* Run lein uberjar in the cljs-rte folder.
* Copy this to the jar file to ~/jars folder as:
```
mkdir ~/jars
mv <cljs-rte-directory>/target/cljs-rte-0.1.0-SNAPSHOT.jar ~/jars/cljs-rte-0.1.0.jar
```
* Deploy the repo locally. Update the DVersion everytime.
```mvn deploy:deploy-file -Dfile=/home/vagrant/jars/cljs-rte-0.1.0.jar -DartifactId=cljs-rte -Dversion=0.1.0 -DgroupId=cljs-rte -Dpackaging=jar -Durl=file:maven_repository -Dmaven.repo.local=maven_repository -DcreateChecksum```
* Copy it to amazon folder.
```aws s3 cp /home/vagrant/jars/maven_repository/cljs-rte/cljs-rte/0.1.0 s3://cljs-rte/releases/cljs-rte/cljs-rte/0.1.0 --recursive```

**Steps to use:**

*  Include it in your project.clj :dependencies section:
``` [cljs-rte/cljs-rte "0.1.0"]```
* And Include in your project.clj :plugins section:
```[s3-wagon-private "1.3.0"] ```
* Add a repositories section to the project.clj
```:repositories {"local" {:url "s3p://cljs-rte/releases/"
:username :env/aws_access_key_id
:passphrase  :env/aws_secret_access_key}}```
* Set env variables
```export AWS_ACCESS_KEY_ID=<some_access_key>
export AWS_SECRET_ACCESS_KEY=<some_secret_key>```  
* `lein deps`

**Steps to run cljs-rte-demo**
* We need /api backend. For this purpose, run api
* `lein sass once` to create/update css files
* `lein figwheel` in the root folder.

**Steps to run test**
* `lein doo phantom test once`

**Production Build:**
**To compile clojurescript to javascript**
```
lein clean
lein cljsbuild once min
```

**References:**
* http://ignaciothayer.com/post/Hosting-Clojure-Dependencies-on-S3/
* https://github.com/s3-wagon-private/s3-wagon-private




