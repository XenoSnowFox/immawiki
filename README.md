# immawiki

## Configuration

### Site Properties

| Property    | Description                                       | Example            |
|-------------|---------------------------------------------------|--------------------|
| NAME        | Name of your Wiki.                                | `My Wiki`          |
| DOMAIN      | Domain name that your wiki is served under.       | `wiki.example.com` |
| PATH_PREFIX | Path that the root of your wiki is served under.  | `/` or `/my-wiki/` |

### Homepage Properties

| Property    | Description                                       | Example                                                                                      |
|-------------|---------------------------------------------------|----------------------------------------------------------------------------------------------|
| TITLE       | Name of your Wiki.                                | `My Wiki`                                                                                    |
| SUMMARY     | Domain name that your wiki is served under.       | `Welcome to my wiki which constain information about the project I am currently developing.` |

## Build and Deploy

```shell
# BUILD
./gradlew

# BOOTSTRAP CDK
cdk bootstrap

# CDK SYNTH
cdk synth 

# DEPLOY
cdk deploy

```

## Running locally with Localstack

```shell
docker run --rm --detach \
    -p 4566:4566 \
    -p 4571:4571 \
    -p 8080:8080 \
    -v ./.localstack/bin:/docker-entrypoint-initaws.d \
    -e AWS_DEFAULT_REGION=ap-southeast-2 \
    -e AWS_ACCESS_KEY_ID=localstack \
    -e AWS_SECRET_ACCESS_KEY=localstack \
    -e EDGE_PORT=4566 \
    -e LAMBDA_EXECUTOR=docker \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --name immawiki \
    localstack/localstack:3.8
```


```shell
docker run --rm --detach -p 4566:4566 -p 4571:4571 -p 8080:8080 -e AWS_DEFAULT_REGION=ap-southeast-2 -e AWS_ACCESS_KEY_ID=localstack -e AWS_SECRET_ACCESS_KEY=localstack  -e EDGE_PORT=4566 -e LAMBDA_EXECUTOR=docker -v /var/run/docker.sock:/var/run/docker.sock --name immawiki localstack/localstack:3.8
```


```shell 
./gradlew clean shadowJar

cdklocal bootstrap

cdklocal synth

cdklocal deploy
```




