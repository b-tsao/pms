FROM mysql:5.5.46

# ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_ALLOW_EMPTY_PASSWORD=yes
ENV MYSQL_DATABASE=argonms

COPY sql/argon.sql sql/default_cash_shop.sql sql/default_shop.sql sql/default_commodities.sql sql/default_npc_scripts.sql sql/default_reactor_scripts.sql /docker-entrypoint-initdb.d/
