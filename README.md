# Инструкция по запуску
 - Создать директорию и скопировать туда файл docker-compose.yml
 - В этой директории создать папку с именем postgres и в ней создать файл Dockerfile. В этот файл вставить строчку 
 > FROM postgres:12-alpine
 - Установать docker-compose v2.6.0
 - Запустить командой 
 > docker-compose up -d
 
 # Curl запросы для взаимодействия с сервером
 - Регистрация
 > curl -L -X POST http://<ip_address>:79/registration -F username="someUser" -F password="somePassword"
 - Аутентификация
 > curl -L -X POST http://<ip_address>:79/authentication -F username="someUser" -F password="somePassword"
 - Отправка сообщения
 > curl -L -X POST http://<ip_address>:79/message -F username="someUser" -F text="<text>" -H authorization:Bearer_<token>
 
 # Возможные ошибки
 - Если возникает ошибка при запуске docker-compose необходимо установить переменную среды командой 
 > export DOCKER_BUILDKIT=0
