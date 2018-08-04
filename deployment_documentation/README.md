# DEPLOYMENT README

In terms of deployment, the general deployment workflow is as follows:

Git Repo -> CI/CD Server -> Docker Registry -> Deployment

For us, it is as follows:

Github -> TeamCity -> Training Provided Registry -> uDeploy + Openshift

## [Github](https://github.com/Manan-Singh/SwiftTrading)

We keep our project routinely updated and tracked on Github. Git is a useful version control system and Github is a useful service for keeping track of changes.
Code is reviewed by team members before pull requests get merged into master.

## [TeamCity](teamcity_build_steps.md)

TeamCity will build our project into containers that will be uploaded to the docker registry provided to us.
It can detect changes to the master branch of our git repository and rebuild our project.

## [Testing in AWS Linux Box](aws_testing_setup.md)

While production support is able to do deployment testing, we tried doing deployment testing of our own to catch issues early on.
Since production support also needs to work with other teams to deploy their projects, this allows us to continue testing.

## Production Support

Production support will handle actual deployment using uDeploy and Openshift.

### Notes:

Dockerfile contains the following:

Specific managed version of java to use:
`FROM dockerreg.training.local:5000/java`

Exposes a port assigned by production support: 
`EXPOSE 8087`

Allows production support to monitor our container using ITRS:
`CMD ["/opt/itrs/netprobe/netprobe.linux_64", "$APPNAME", "-port", "7036", "-nopassword", "&"]`

Production support also gave us [environment variables](environment_variables.md) to incorporate.
