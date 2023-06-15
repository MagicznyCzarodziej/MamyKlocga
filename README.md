# MamyKloc.ga

## How tu run

- Build frontend (run `yarn build` in /frontend)
- Copy files from /frontend/dist to /resources/static

### Locally
```shell
java -Dspring.profiles.active=local -jar BUILT_FILE_NAME.jar
```

### Production / On hosting
Get certificate file:
- Get .pem and .key from hosting and convert it to keystore file (.jks) 
```shell
openssl pkcs12 -export -in cert.pem -inkey cert.pem -out cert.p12 -name "certificate"
```
- Put cert.jks file in /resources
- Fill passwords to keystore in application-prod.yml
- Build JAR using bootJar task
- Upload JAR from /build/libs to hosting
- run on hosting: 
```shell
export JAVA_VERSION="17"
java -Dspring.profiles.active=prod -jar BUILT_FILE_NAME.jar 
```
- Remember to switch on Force SSL in Proxy domain details in hosting settings 