<p align="center">
  <h1 align="center">ShareIt</h1>
  <p align="center">
    Share things and find the ones you need!
    <br/>
    <a href="https://github.com/Zazergel/java-shareit/issues">Report Bug</a> *
    <a href="https://github.com/Zazergel/java-shareit/issues">Request Feature</a>
  </p>
</p>
<div class="header" markdown="1" align="center">

  ![Downloads](https://img.shields.io/github/downloads/Zazergel/java-shareit/total) 
  ![Contributors](https://img.shields.io/github/contributors/Zazergel/java-shareit?color=dark-green) 
  ![Forks](https://img.shields.io/github/forks/Zazergel/java-shareit?style=social) 
  ![Issues](https://img.shields.io/github/issues/Zazergel/java-shareit) 
</div>


## Table Of Contents

* [About the Project](#about-the-project)
* [Endpoints](#endpoints)
* [DB scheme](#db-scheme)
* [Built With](#built-with)
* [Installation](#installation)
* [Authors](#authors)

## About The Project

Проект обеспечивает пользователям, во-первых, возможность рассказывать, какими вещами они готовы поделиться, а во-вторых, находить нужную вещь и брать её в аренду на какое-то время. 
Функционал сервиса не только позволяет бронировать вещь на определённые даты, но и закрывает к ней доступ на время бронирования от других желающих. На случай, если нужной вещи на сервисе нет, у пользователей есть возможность оставлять запросы. *(Вдруг древний граммофон, который странно даже предлагать к аренде, неожиданно понадобится для театральной постановки.)* По запросу можно добавлять новые вещи для шеринга. 

Данный проект реализован на основе микросервисной архитектуры. Первоначально запрос от пользователя приходит на сервис **gateway**, который осуществляет валидацию запроса. Если валидация прошла успешно, то gateway пропускает запрос на основной сервис - **server**, который содержит основную логику и осуществляет взаимодействие с базой данных **Postgres**. 
Каждое из приложений запускается как самостоятельное Java-приложение в собственном **Docker-контейнере**, а их общение происходит через REST. 

## Endpoints

<details>
  <summary><b>Users</b></summary>
  
 - ```[GET] /users``` – получить список всех пользователей 
 - ```[GET] /users/{id}```  – получить пользователя ```id``` 
 - ```[POST] /users```  – создать нового пользователя 
- ```[PATCH] /users/{id}```  – обновить пользователя ```id``` 
 - ```[DELETE] /users/{id}```  - удалить пользователя ```id``` 
</details>
<details>
  <summary><b>Items</b></summary>
  
  Идентификатор пользователя передается в заголовке ```X-Sharer-User-Id```<br><br>
  - ```[GET] /items?from={from}&size={size}``` – получить постраничный список всех вещей пользователя (с комментариями)
  - ```[GET] /items/{id}``` – получить вещь ```id``` (с комментариями)
  - ```[POST] /items``` – создать вещь и привязать к текущему пользователю
  - ```[PUT] /items/{id}``` – обновить вещь ```id``` текущего пользователя
  - ```[DELETE] /items/{id}``` - удалить вещь ```id``` принадлежащую текущему пользователю
  - ```[GET] /items/search?text={text}&from={from}&size={size}``` – найти и вывести постранично все вещи, имеющие ```text``` в имени или описании, и доступные для запроса
  - ```[POST] /items/{id}/comment``` – оставить комментарий после использования вещи ```id```
</details>
<details>
  <summary><b>Booking</b></summary>
  
  Идентификатор пользователя передается в заголовке ```X-Sharer-User-Id```<br><br>
  - ```[GET] /bookings?state={state}&from={from}&size={size}``` – получить постранично список всех бронирований пользователя со статусом ```state```
- ```[GET] /bookings/{id}``` – получить бронирование ```id``` (доступно только владельцу вещи и автору бронирования)
- ```[GET] /bookings/owner?state={state}&from={from}&size={size}``` – получить постранично список всех бронирований всех вещей владельца со статусом ```state```
- ```[POST] /bookings``` – забронировать вещь
- ```[PATCH] /bookings/{id}``` – обновить статус бронирования вещи ```id```
</details>
<details>
  <summary><b>Request</b></summary>

  Идентификатор пользователя передается в заголовке ```X-Sharer-User-Id```<br><br>
- ```[POST] /requests``` – создать новый запрос вещи
- ```[GET] /requests``` – получить список запросов текущего пользователя (с ответами на них)
- ```[GET] /requests/{id}``` – получить запрос ```id``` (с ответами на него)
- ```[GET] /requests/all?from={from}&size={size}``` – получить постраничный список запросов, созданных другими пользователями (с ответами на них)

</details>

## DB scheme

<details>
  <summary><b>Schema of data base</b></summary>
 <img src='https://i.postimg.cc/Tw31t9Ln/Share-It-1.png' border='0' alt='Share-It-1'/>
</details>



## Built With
<p align="left">
    <img src="https://skillicons.dev/icons?i=java,maven,spring,postgres,hibernate,docker" />
</p>

### Installation

1. Склонируйте репозиторий
```sh
git clone https://github.com/Zazergel/java-shareit.git
```
2. Для сборки проекта используйте [Maven](https://maven.apache.org/)
3. Установите [Docker](https://www.docker.com/products/docker-desktop/)
4. Соберите проект, выполнив команду:
```sh
mvn clean install
 ```
6. Запустите проект с помощью
```sh
docker-compose up
```


## Authors

 **[Zazergel](https://github.com/Zazergel/)**
