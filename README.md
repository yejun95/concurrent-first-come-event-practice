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
```
<br>
<hr>
<br>

**Reference**<br>

[최상용 : 실습으로 배우는 선착순 이벤트 시스템](https://www.inflearn.com/course/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5/dashboard)
