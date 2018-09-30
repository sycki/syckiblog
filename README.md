# syckiblog
syckiblog is a blog web.

## Usage
### Build
```
git clone https://github.com/sycki/syckiblog.git
cd syckiblog
mvn clean package
```

### Prepare the database
Change database connect password first in config file: `src/main/resources/application.properties`

```
mysql -u root -p < sql/syckiblog.sql
```

### Run
```
cp src/main/resources/syckiblog.conf target/
java -jar target/*.jar
```

### Visit
```
curl localhost:8080
```

### Login and management
Visit url `localhost:8080/login` in your browser.
* user: admin
* pass: admin
