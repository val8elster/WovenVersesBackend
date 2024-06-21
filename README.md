# Woven Verses Backend

## 1. Summary

The backend is responsible for data storage (MySQL database), user authentication and the leaderboard. It communicates via REST with the frontend. It is currently accessible through [elster.dev](https://elster.dev:8787), but can also be hosted locally.\
The [database](https://h2881275.stratoserver.net:8443/phpmyadmin) (username: mobcomp, password: 501ovWb&8) stores a user's user-id (primary key), username and mail (unique), their password, the time for each chapter, whether the user completed the intro and their placement in the ranking.\
For a more detailed documentation, including mappings, more information about the database and the process of public hosting, visit the GitHub repository’s [README](https://github.com/val8elster/WovenVersesBackend/blob/main/README.md). 


## 2. SQL

~~~mysql
CREATE TABLE users (
		id    BIGINT(20)  PRIMARY KEY,
		email    VARCHAR(255)  UNIQUE,
		name    VARCHAR(255)  UNIQUE,
		rank    INT(11)  DEFAULT 0,
		time1    FLOAT    DEFAULT 0,
		time2    FLOAT    DEFAULT 0,
		time3    FLOAT    DEFAULT 0,
		time4    FLOAT    DEFAULT 0,
		time5    FLOAT    DEFAULT 0,
		time6    FLOAT    DEFAULT 0,
		time7    FLOAT    DEFAULT 0,
		password    VARCHAR(255)    DEFAULT “pw”,
		intro_completed    BIT(1)    DEFAULT B(0)
)
~~~

## 3. Deployment

### 3.1. Local Deployment 

WovenVersesBackend is a GitHub repository. It is located [here](https://github.com/val8elster/WovenVersesBackend). To run the backend, it must first be cloned from GitHub:

~~~git
git clone https://github.com/val8elster/WovenVersesBackend
~~~

and opened in any IDE supporting Kotlin, such as IntelliJ. WovenVersesBackend uses Maven to manage the dependencies, they will be installed automatically upon building the project. 

#### 3.1.1. Required changes to frontend when deploying locally

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <!-- <domain includeSubdomains="true">10.0.2.2</domain> --> <!-- for local hosting -->
        <domain includeSubdomains="true">elster.dev</domain> <!-- for remote hosting --> 
    </domain-config>
</network-security-config>
~~~

~~~kotlin
object ApiClient {
    //private const val BASE_URL = "http://10.0.2.2:8787/" //for local hosting
    private const val BASE_URL = "https://elster.dev:8787/" //for remote hosting

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
~~~

In app/src/main/res/xml change the includeSubdomains adress parameter and in app/src/main/java/com/example/verseverwebt/api change the BASE_URL to 10.0.2.2, as shown above, since that is what the Android Emulator identifies the Computer's localhost as.

### 3.2. Public Hosting

For public hosting, data security is essential. In this case the backend is being hosted on a self-owned server, linked to the domain [elster.dev](https://elster.dev). This domain is protected by a SSL-Certificate by [Lets-Encrypt](https://letsencrypt.org/). For Springboot, which the backend utilizes, the certificate has to be converted into a KeyStore through Keytool:  

~~~
keytool -import -alias springboot -file myCertificate.crt -keystore keystore.p12 -storepass 501ovWb&8 
~~~

Next, the application.properties file of the backend must be modified to accommodate the following paramters:

~~~
security.require-ssl=true
server.ssl.key-store: keystore.p12
server.ssl.key-store-password: 501ovWb&8
server.ssl.keyStoreType: PKCS12
server.ssl.keyAlias: tomcat
~~~

#### 3.2.1. Creating a .jar file

To avoid having to copy and install all the dependecies on the server, you can build a .jar file. Building a .jar file works regardless of having a SSL-certificate or not, and it is a snapshot of the current state of the backend.

##### 3.2.1.1. Requirements

JDK/JRE17 or higher:

~~~
sudo apt install openjdk-17-jdk openjdk-17-jre
~~~

Maven:

~~~
sudo apt install maven -y
~~~

WovenVersesBackend's .jar file was created with WSL Ubuntu 22.04 LTS.

#### 3.2.2. Building the .jar

Navigate into the root directory of WovenVersesBackend.

There, run:

~~~
mvn clean package
~~~

![](https://github.com/val8elster/WovenVersesBackend/assets/130545294/85b6fffe-88e2-476f-9b68-1af971bcca21)

The .jar file should now be in the /target directory.

#### 3.2.3. Launching the .jar

Navigate into the target directory:

~~~
cd target
~~~

Now launch the .jar file:

~~~
java -jar VVBackend-0.0.1-SNAPSHOT.jar
~~~

#### 3.2.4. Notable information

For external hosting on a remote server, the keystore.pk12 has to be placed where the application.properties specifies, and starting the backend is recommended using the screen command additional to the java command:

~~~
screen java -jar VVBackend-0.0.1-SNAPSHOT.jar
~~~

Now you can access the backend through localhost: http(s)://localhost:8787 or your server: http(s)://server-ip:8787 with REST.

(Why port 8787? That's also declared in the application.properties file, and in the WovenVersesBackend it is 8787. Changing it there means also having to change it in the frontend. This works the same as changing the IP / switching between localhost and a remote host. (see: 3.1.1.))

## 4. Mappings

You can test all the mappings without the frontend through a tool like [postman](https://www.postman.com/).

### 4.1. GET-Mappings

~~~
GET /users
~~~
Parameters: none \
Response: all users, not sorted  
  
~~~
GET /users/rankings
~~~
Parameters: none \
Response: all users, sorted by ranking  
  
~~~
GET /users/{id}/chapter/{chapter}/time
~~~
Parameters: id of user, chapter to be viewed \
Response: float time of chapter  
  
~~~
GET /users/{id}intro
~~~
Parameters: id of user \
Response: boolean chapter_intro  
  
~~~
GET /users/name/{name}
~~~
Parameters: name of possible user \
Response: boolean ifExists  
  
~~~
GET /users/mail{mail}
~~~
Parameters: mail of possible user \
Response: boolean ifExists  
  
### 4.2. POST-Mapping

~~~
POST /users
~~~
Parameters: none \
Request Body:
~~~json
"user":{
        "id": 1001,
        "name": "$name",
        "password": "$password",
        "mail": "$mail",
        "time1": 0,
        "time2": 0,
        "time3": 0,
        "time4": 0,
        "time5": 0,
        "time6": 0,
        "time7": 0,
        "ranking": 0,
        "intro_completed": 0
},
~~~~
Response: Unit  
  
### 4.3. PUT-Mappings

~~~
PUT /users/calculate-rankings
~~~
Parameters: none \
Response: Unit (calculates Rankings)  
  
~~~
PUT /users/{id}/chapter{chapter}/time
~~~
Parameters: id of user, chapter to be changed \
Request Body:
~~~json
"time": "$time"
~~~~
Response: Unit  
  
~~~
PUT /users/{id}/intro
~~~
Parameters: id of user \
Response: Unit (sets intro_completed to true)  
  
