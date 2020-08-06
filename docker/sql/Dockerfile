FROM mysql:5.5.46

# ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_ALLOW_EMPTY_PASSWORD=yes
ENV MYSQL_DATABASE=vintage

COPY sql/vintage.sql /docker-entrypoint-initdb.d/
