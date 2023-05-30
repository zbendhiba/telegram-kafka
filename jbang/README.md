cat s3.properties | envsubst > application.properties

camel run S3ToKnative.groovy

rm application.properties
