# Drools 

## Installing and Running
- Kafka Cluster on Openshift
https://github.com/desmax74/openshift-handbook/blob/master/fedora/kafka.md

In the root of the project
```sh
mvn clean package
```
### API

- http://<address>/rest/env/all  env vars

#### Build Container on docker
In the springboot module
```sh
docker build -t quickstarter/openshift-kie-jdkhttp:latest .
docker images | grep openshift-kie
```

#### Deploy on Openshift
Relax RBAC for configmap
```sh
kubectl create clusterrolebinding permissive-binding --clusterrole=cluster-admin --group=system:serviceaccounts
```

By default will be created under project called "My Project"
```sh
kubectl create -f kubernetes/deployment.yaml 
kubectl create -f kubernetes/service.yaml 
oc expose service  openshift-kie-jdkhttp
```

 ```
 oc get route
 
  NAME           HOST/PORT                                      PATH      SERVICES       PORT      TERMINATION   WILDCARD
  openshift-kie   openshift-kie-my-kafka-project.192.168.99.109.nip.io           openshift-kie   http                    None
  ```
  
  Your address will be
  http://quick-drools-myproject.192.168.99.109.nip.io/rest/env/all
  
### Remote debug    
    
#### Using docker hub registry
```sh
docker login --username=<user username>
docker build -t <user_username>/openshift-kie-jdkhttp:<tag> .  
docker push <user_username>/openshift-kie-jdkhttp:<tag>
```

#### Deploy
With version 0.3 of desmax74/openshift-kie-jdkhttp
```sh
kubectl create -f kubernetes/debug_pod.yaml
kubectl create -f kubernetes/deployment_registry.yaml
```

#### Port forward
port forwarding 
```sh
oc port-forward <POD> 8000 3000 3001
```
```sh
jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=8000
```

#### Visualvm
visualvm --openjmx localhost:3000

#### IntellijIdea
Attach to process




