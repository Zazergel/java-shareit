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
* [Built With](#built-with)
* [Installation](#installation)
* [Authors](#authors)

## About The Project

Проект обеспечивает пользователям, во-первых, возможность рассказывать, какими вещами они готовы поделиться, а во-вторых, находить нужную вещь и брать её в аренду на какое-то время. 
Функционал сервиса не только позволяет бронировать вещь на определённые даты, но и закрывает к ней доступ на время бронирования от других желающих. На случай, если нужной вещи на сервисе нет, у пользователей есть возможность оставлять запросы. *(Вдруг древний граммофон, который странно даже предлагать к аренде, неожиданно понадобится для театральной постановки.)* По запросу можно добавлять новые вещи для шеринга. 

Данный проект реализован на основе микросервисной архитектуры. Первоначально запрос от пользователя приходит на сервис **gateway**, который осуществляет валидацию запроса. Если валидация прошла успешно, то gateway пропускает запрос на основной сервис - **server**, который содержит основную логику и осуществляет взаимодействие с базой данных **Postgres**. 
Каждое из приложений запускается как самостоятельное Java-приложение в собственном **Docker-контейнере**, а их общение происходит через REST. 

## Built With
<p align="left">
    <img src="https://skillicons.dev/icons?i=java,maven,spring,postgres,hibernate,docker" />
</p>

### Installation

1. Склонируйте репозиторий

```sh
git clone https://github.com/Zazergel/java-shareit.git
```

2. Установите [Docker](https://www.docker.com/products/docker-desktop/)

3. Запустите проект с помощью Docker-compose.


## Authors

 **[Zazergel](https://github.com/Zazergel/)**
