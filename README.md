NOTA SOBRE ESTE REPOSITORIO

Este repositorio lo voy a estar utilizando para crear una api con autenticación mediante Java Web Tokens, con el objetivo de aprender sobre métodos de seguridad para aplicaciones.
Haré una serie de pasos para la prueba de estos tokens (muy sencillo, es para entender su funcionamiento)

COMO PROBARLO (En este caso, con ThunderClient)

Register:
Enviamos una Request POST a "/api/auth/register", donde insertamos en el Body Un JSON tal que:

{"email": "mi@email.com", "password": "mipassword" }

(El password debe contener como mínimo 8 caracteres)
Esta petición nos devolverá nuestro Token.

Login:
Enviamos una Request POST a "/api/auth/login", donde insertamos en el Body Un JSON tal que:

{"email": "mi@email.com", "password": "mipassword" }

Nos devolverá nuestro token

Para utilizar el token y ver todos los usuarios:

Nos dirigiremos a la sección Headers (entre "Auth" y "Query") y colocaremos:

Key - Value
Authorization - Nuestro_token

Con una request GET obtendremos los usuarios

TODOS MIS REPOSITORIOS SON PROYECTOS DE PRÁCTICA A MENOS QUE SE INDIQUE LO CONTRARIO. TODO PURAMENTE ACADÉMICO Y AUTODIDACTA.

