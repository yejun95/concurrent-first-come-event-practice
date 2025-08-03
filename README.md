# 쿠폰을 활용한 선착순 이벤트 시스템
<br>
<hr>
<br>

## ✔️ mysql for docker
- docker를 이용하여 mysql 사용

- 로컬에서 3306, 3307 포트를 사용중이기에 3308 -> 3306으로 맵핑
```
docker run -d -p 3308:3306 --name mysql -e MYSQL_ROOT_PASSWORD=1234 mysql

docker exec -it mysql bash

bash-5.1# mysql -u root -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 9
Server version: 9.3.0 MySQL Community Server - GPL

mysql> create database coupon_example;
Query OK, 1 row affected (0.007 sec)

mysql> use stock_example
Database changed
mysql>
```
<br>
<hr>
<br>

## ✔️ redis for docker
- docker를 사용하여 redis 사용

- 분산락 구현을 위한 Lettuce, Redisson 사용 예정

```
docker pull redis

docker run --name myredis -d -p 6379:6379 redis

docker ps
CONTAINER ID   IMAGE     COMMAND                   CREATED         STATUS          PORTS                               NAMES
003d7266f1a4   redis     "docker-entrypoint.s…"   2 seconds ago   Up 1 second     0.0.0.0:6379->6379/tcp              myredis
f1b27c48c8bd   mysql     "docker-entrypoint.s…"   26 hours ago    Up 12 minutes   33060/tcp, 0.0.0.0:3308->3306/tcp   mysql

docker exec -it 003d7266f1a4 redis-cli

$ 127.0.0.1:6379> incr coupon_count
```

- `sadd`를 활용한 set 자료구조 사용법

```
127.0.0.1:6379> sadd test 1
(integer) 1
127.0.0.1:6379> sadd test 1
(integer) 0
127.0.0.1:6379> sadd test 1
(integer) 0
```
> set 자료구조는 중복 키값을 허용하지 않기 때문에 이미 값이 존재하면 0을 리턴한다.
<br>
<hr>
<br>

## ✔️ zoopeeker and kafka for docker-compose
- `docker-compose.yml` 파일 생성

```yml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNET: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```
<br>

- 파일이 있는 디렉토리에서 compose 명령어 실행

```
$ docker-compose up -d
```
<br>

- 토픽 생성
```
$ docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic testTopic
```
<br>

- 프로듀서 실행
```
$ docker exec -it kafka kafka-console-producer.sh --topic testTopic --broker-list 0.0.0.0:9092
```
<br>

- 컨슈머 실행
```
$ docker exec -it kafka kafka-console-consumer.sh --topic testTopic --bootstrap-server localhost:9092
```
<br>

- kafkaTemplate을 사용한 Consumer 실행
```
docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```
<br>
<hr>
<br>

**Reference**<br>

[최상용 : 실습으로 배우는 선착순 이벤트 시스템](https://www.inflearn.com/course/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5/dashboard)
