camel run integration.yml
curl http://localhost:8080/hello

cat s3.properties | envsubst > application.properties

camel run S3ToKnative.groovy

rm application.properties
