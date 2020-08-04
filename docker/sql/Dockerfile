FROM mysql:5.5.46

# ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_ALLOW_EMPTY_PASSWORD=yes
ENV MYSQL_DATABASE=argonms

COPY sql/argon.sql /docker-entrypoint-initdb.d

EXPOSE 3306
