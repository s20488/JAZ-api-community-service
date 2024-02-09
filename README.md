# Spring REST API z JSONPlaceholder i HSQLDB

Projekt jest oparty na Spring REST API, który współpracuje z danymi z JSONPlaceholder i używa bazy danych HSQLDB.

## Uruchamianie lokalnej bazy danych
Wpisz poniższe polecenie w konsoli w środowisku:

```bash
java -cp "...\hsqldb\lib\hsqldb.jar" org.hsqldb.server.Server --database.0 "file:communitydb" --dbname.0 communitydb --port 9002
```
