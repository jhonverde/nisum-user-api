# Nisum Ejercicio Java

El proyecto contiene:

- JUnit para las pruebas unitarias de la capa web, service y persistencia.
- Javax Validation para la validación de los "request".
- Integración con Hibernate aplicando el patrón DAO.
- H2 Database  
- Generación token JWT para la activación de un usuario.
- Spring Actuator para obtener métricas del aplicativo.
- Spring Security para securizar los endpoints a través de un token JWT. Si está activado el profile local, no es necesario enviarlo. 

# Pasos

Ejecutar para descargar las dependencias, generar el jar y ejecutar los tests.

`./gradlew build`

Levantar el aplicativo por defecto se levanta con profile "local"

`./gradlew bootRun`

Si se quiere levantar con otro profile por ejemplo "qa".

`./gradlew bootRun --args='--spring.profiles.active=qa'`

# Pruebas

Nota: Si se levanta el aplicativo con profile "qa" se debe enviar el token JWT. Para pruebas se creo un endpoint que te brinda el access token.

`curl http://localhost:8085/token`

Para probar con postman importar el NISUM-USER-API.postman.collection.json

Para probar con CURL ejecutar:

findAllUsers:

`curl http://localhost:8085/api/v1/users`

findUserById

`curl http://localhost:8085/api/v1/users/dc77c429-17ba-47ac-bb8a-1b0cf3614334`

saveUser 

`curl -d @src/test/resources/json/saveUserRequest.json -H "Content-Type: application/json" http://localhost:8085/api/v1/users`

updateUser

`curl -d @src/test/resources/json/updateUserRequest.json -H "Content-Type: application/json" -X PATCH http://localhost:8085/api/v1/users/dc77c429-17ba-47ac-bb8a-1b0cf3614334`

deleteUser

`curl -v -X DELETE http://localhost:8085/api/v1/users/dc77c429-17ba-47ac-bb8a-1b0cf3614334`