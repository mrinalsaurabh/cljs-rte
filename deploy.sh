lein uberjar;

rm -rf ~/jars/
mkdir ~/jars
mv target/cljs-rte-0.1.0-SNAPSHOT.jar ~/jars/cljs-rte-0.1.1.jar;

mvn deploy:deploy-file -Dfile=/home/vagrant/jars/cljs-rte-0.1.1.jar -DartifactId=cljs-rte -Dversion=$1 -DgroupId=cljs-rte -Dpackaging=jar -Durl=file:maven_repository -Dmaven.repo.local=maven_repository -DcreateChecksum;

aws s3 cp maven_repository/cljs-rte/cljs-rte/$1 s3://cljs-rte/releases/cljs-rte/cljs-rte/$1 --recursive
