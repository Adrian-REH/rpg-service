docker run -d --name zookeeper -p 2181:2181 zookeeper:3.8

docker run -d --name kafka -p 9092:9092 \
-e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
bitnami/kafka:latest