#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home
export JAVA_HOME=/usr/local/opt/openjdk@11/
archive="be-backend-`date '+%Y%m%d%H%M'`.jar"
echo $archive
cd be-parent
mvn clean package  -Dmaven.test.skip=true   ;
cd ..
scp -P 57716 be-backend/target/be-backend-1.0.0-SNAPSHOT.jar shilei@aidev.tigercloud.com.cn:/data/incoming/$archive
echo "rm -f /data/app/be-backend/lib/be-backend-1.0.0.jar;ln -s /data/incoming/$archive /data/app/be-backend/lib/be-backend-1.0.0.jar" | ssh -p 57716 shilei@aidev.tigercloud.com.cn
