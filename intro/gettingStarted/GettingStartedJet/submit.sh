#!/usr/bin/env bash

docker run -p 5701:5701 hazelcast/hazelcast-jet & \
docker run -it -v /run/media/sudipto/Seagate/IntelliJProjects/hazelcastTraining/intro/gettingStarted/GettingStartedJet/target:/jars hazelcast/hazelcast-jet jet -a 172.17.0.1 submit /jars/GettingStartedJet-1.0-SNAPSHOT.jar && fg
