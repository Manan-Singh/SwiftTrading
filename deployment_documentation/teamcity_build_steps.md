## TeamCity Build Steps

TeamCity is used as our build management and continuous integration server.

### Front End Build

For deployment, we combine our Angular front end (swift-app) with our REST API (ApiGateway). Normally these will be in separate containers but due to time constraints this approach was picked.

#### Build Step 1: Angular

Angular front end setup. Install necessary files, build the project, and move into static directory under ApiGateway.

```
#!/bin/bash

npm --prefix swift-app install 
npm --prefix swift-app run ng build --prod
mkdir ApiGateway/src/main/resources/static
mv swift-app/dist/* ApiGateway/src/main/resources/static/
echo "setup complete"
```

#### Build Step 2: Maven

Maven clean package. This is done using the ApiGateway/pom.xml file.

#### Build Step 3: Docker Build 

Build and tag image.

```
#!/bin/bash

docker build -t swift-rest-ui .
```

#### Build Step 4: Push to Docker Registry

Tag and push image to docker registry. Additionally, tag and push the image as latest. 

```
#!/bin/bash

if docker tag swift-rest-ui dockerreg.training.local:5000/swift-rest-ui:%build.number%
then
  docker push dockerreg.training.local:5000/swift-rest-ui:%build.number%
  docker tag dockerreg.training.local:5000/swift-rest-ui:%build.number% dockerreg.training.local:5000/swift-rest-ui:latest
  docker push dockerreg.training.local:5000/swift-rest-ui:latest
else
  echo "FAILED to push the image to the Docker Registry" 1>&2
fi
```

#### Build Step 5: Clean Up

Remove the image on TeamCity and remove the image build number on the registry. Only the image with latest tag will be on the registry.

```
#!/bin/bash

docker rmi swift-rest-ui
docker rmi dockerreg.training.local:5000/swift-rest-ui:%build.number%
```


### Trade Engine Build

For deployment, we deploy our trade engine in its own container.

#### Build Step 1: Maven

Maven clean package. This is done using the TradeEngine/pom.xml file.

#### Build Step 2: Docker Build 

Build and tag image.

```
#!/bin/bash

docker build -t swift-engine .
```

#### Build Step 3: Push to Docker Registry

Tag and push image to docker registry. Additionally, tag and push the image as latest. 

```
#!/bin/bash

if docker tag swift-engine dockerreg.training.local:5000/swift-engine:%build.number%
then
  docker push dockerreg.training.local:5000/swift-engine:%build.number%
  docker tag dockerreg.training.local:5000/swift-engine:%build.number% dockerreg.training.local:5000/swift-engine:latest
  docker push dockerreg.training.local:5000/swift-engine:latest
else
  echo "FAILED to push the image to the Docker Registry" 1>&2
fi
```

#### Build Step 4: Clean Up

Remove the image on TeamCity and remove the image build number on the registry. Only the image with latest tag will be on the registry.

```
#!/bin/bash

docker rmi swift-engine
docker rmi dockerreg.training.local:5000/swift-engine:%build.number%
```