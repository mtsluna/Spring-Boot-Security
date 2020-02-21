# Security module
This is a simple app to incorporate security in your project.

## Incorporate
For use this project you only need fork or clone this repository and you have a simple project with security incorporated.
Or for incorporate this functionality in your project do you need follow the next steps:
- Copy the folders located in java/ml/corp/security without the file 'DemoApplication.java' into your folder java.
- Add in your POM.XML the next dependencies:
``` xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
  </dependency>
  <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20160810</version>
  </dependency>
</dependencies>
```
- Don't forget add the conection at your database, this project have JPA and MySQL but you can use PostgresSQL or other.
## Use
This projects have 3 urls thats permit the correct functionality of the security.
### Register a new user
``` curl
POST {path}/api/auth/register
```
Body:
``` json
{
	"username": "test@gmail.com",
	"password": "test"
}
```
Output:
- User created: 201 - Created, without body
- User already exist: 400 - Bad Request, without body.
### New token
``` curl
POST {path}/api/auth/new/token
```
Body:
``` json
{
	"username": "test@gmail.com",
	"password": "test"
}
```
Output:
- If the credentials are valid:
``` json
{
    "status": "Authorized",
    "access_token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1ODIyNDkzMDgsImV4cCI6MTU4MjI0OTQ4OH0.wj_efg0e6saDl4z6Df7UqBr5wjIhKVOd3NZqS-6qfUbjBbydMgQOY2GPT_-9HWR7AaxxHDuz4XMdGIHsHDQTJw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJzY29wZSI6InJlZnJlc2giLCJ1c2VybmFtZSI6IjVlNWUwNjNlNTNiY2E3MTgzZTQ1ZWMwMGMxZTVhODQ5IiwiaWF0IjoxNTgyMjQ5MzA4LCJleHAiOjE1ODIyNDk2Njh9.NhHDjBJsQijorRHZLhTAKkSvtWObT9aj-tyCHrymUf1cZlBdLaOaKc0r-KpNrhmFMpI3zNySvNhHNnTbXeYjCw",
    "expires_on": 1800
}
``` 
- If the credentials are invalid or other error:
``` json
{
    "status": "Not authorized",
    "reason": "X"
}
```
### Refresh token
In this request you need use the refresh token given for the server when you generate a new token.
``` curl
POST {path}/api/auth/refresh/token
```
Body:
``` json
{
	"refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJzY29wZSI6InJlZnJlc2giLCJ1c2VybmFtZSI6IjVlNWUwNjNlNTNiY2E3MTgzZTQ1ZWMwMGMxZTVhODQ5IiwiaWF0IjoxNTgyMjQ5MzA4LCJleHAiOjE1ODIyNDk2Njh9.NhHDjBJsQijorRHZLhTAKkSvtWObT9aj-tyCHrymUf1cZlBdLaOaKc0r-KpNrhmFMpI3zNySvNhHNnTbXeYjCw"
}
```
Output:
- If the refresh_token is valid:
``` json
{
    "status": "Authorized",
    "access_token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1ODIyNDkzMDgsImV4cCI6MTU4MjI0OTQ4OH0.wj_efg0e6saDl4z6Df7UqBr5wjIhKVOd3NZqS-6qfUbjBbydMgQOY2GPT_-9HWR7AaxxHDuz4XMdGIHsHDQTJw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJzY29wZSI6InJlZnJlc2giLCJ1c2VybmFtZSI6IjVlNWUwNjNlNTNiY2E3MTgzZTQ1ZWMwMGMxZTVhODQ5IiwiaWF0IjoxNTgyMjQ5MzA4LCJleHAiOjE1ODIyNDk2Njh9.NhHDjBJsQijorRHZLhTAKkSvtWObT9aj-tyCHrymUf1cZlBdLaOaKc0r-KpNrhmFMpI3zNySvNhHNnTbXeYjCw",
    "expires_on": 1800
}
Output:
- If the refresh_token is not valid:
``` json
{
    "status": "Authorized",
    "access_token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE1ODIyNDkzMDgsImV4cCI6MTU4MjI0OTQ4OH0.wj_efg0e6saDl4z6Df7UqBr5wjIhKVOd3NZqS-6qfUbjBbydMgQOY2GPT_-9HWR7AaxxHDuz4XMdGIHsHDQTJw",
    "refresh_token": "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI1ZTVlMDYzZTUzYmNhNzE4M2U0NWVjMDBjMWU1YTg0OSIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJzY29wZSI6InJlZnJlc2giLCJ1c2VybmFtZSI6IjVlNWUwNjNlNTNiY2E3MTgzZTQ1ZWMwMGMxZTVhODQ5IiwiaWF0IjoxNTgyMjQ5MzA4LCJleHAiOjE1ODIyNDk2Njh9.NhHDjBJsQijorRHZLhTAKkSvtWObT9aj-tyCHrymUf1cZlBdLaOaKc0r-KpNrhmFMpI3zNySvNhHNnTbXeYjCw",
    "expires_on": 1800
}
``` 
- If the credentials are invalid or other error:
``` json
{
    "status": "Not authorized",
    "reason": "X"
}
```
## Use token in a protected resources
In this example we send a simple request to the next url:
``` curl
GET {path}/example
```
For use the token you need to send the next header in the request:
| Key | Value |
| ---------- | ---------- |
| Authorization  | Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIwMDAwMDAwMC0wMDAwLTAwMGMtMDAwMC0wMDAwMDAwMDAwMGQiLCJzdWIiOiI3MWQ3MDhlNDRiMmFkNjJhNmEyMzgyOTVhZDA0MzBiOCIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJzY29wZSI6InJlZnJlc2giLCJ1c2VybmFtZSI6IjcxZDcwOGU0NGIyYWQ2MmE2YTIzODI5NWFkMDQzMGI4IiwiaWF0IjoxNTgyMjQ5MzQyLCJleHAiOjE1ODIyNDk3MDJ9.F-yUosEjDHcyGUOK3kE_dJDV3IkB5PqNGvTaHFaQn_toSyyv4W3-qoKfxjJ_TBjsAuVtl03nO-W8PzLo36jwDQ   |

Output:
- If the token is valid:
Status 200 - OK
``` json
{
    "say": "all it´s ok."
}
```
- If the token is expired or is not valid: 
Status 403 - Forbidden
``` json
{
    "timestamp": "2020-02-21T02:46:13.197+0000",
    "status": 403,
    "error": "Forbidden",
    "message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
    "path": "/example"
}
```
