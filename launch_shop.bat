::
:: ArgonMS MapleStory server emulator written in Java
:: Copyright (C) 2011-2013  GoldenKevin
::
:: This program is free software: you can redistribute it and/or modify
:: it under the terms of the GNU Affero General Public License as
:: published by the Free Software Foundation, either version 3 of the
:: License, or (at your option) any later version.
::
:: This program is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
:: GNU Affero General Public License for more details.
::
:: You should have received a copy of the GNU Affero General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
::

@echo off
@title Shop Server Console
set CLASSPATH=dist\argonms.jar;dist\bcprov-jdk15.jar;dist\js.jar;dist\mysql-connector-java-bin.jar
java -Xmx600m -Dargonms.shop.config.file=shop.properties ^
-Djava.util.logging.config.file=logging.properties ^
-Dargonms.db.config.file=db.properties ^
-Dargonms.ct.macbanblacklist.file=macbanblacklist.txt ^
-Dargonms.shop.blockedserials.file=cashshopblockedserialnumbers.txt ^
-Dargonms.shop.commodityoverride.file=cashshopcommodityoverrides.txt ^
-Dargonms.shop.limitedcommodity.file=cashshoplimitedcommodities.txt ^
-Dargonms.data.dir=wz\ ^
argonms.shop.ShopServer
pause