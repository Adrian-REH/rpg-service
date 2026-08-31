
docker pull gvenzl/oracle-xe:21-slim



docker run -d --name oracle-xe \
-p 1521:1521 \
-p 5500:5500 \
-e ORACLE_PASSWORD=MiPasswordSegura123 \
gvenzl/oracle-xe:21-slim


docker ps

docker exec -it oracle-xe sqlplus system/MiPasswordSegura123@//localhost:1521/XEPDB1


docker run -d --name oracle-xe \
-p 1521:1521 -p 5500:5500 \
-e ORACLE_PASSWORD=MiPasswordSegura123 \
-v /ruta/local/oracle-data:/opt/oracle/oradata \
gvenzl/oracle-xe:21-slim
