# springboot-restful-webservices

# Configuração do Projeto

Este documento descreve os passos necessários para configurar e executar o projeto springboot-restful-webservice.
Tecnologias utilizadas: Java com springboot 3, redis, mysql, docker, docker-compose

## Pré-requisitos

Antes de começar, certifique-se de que você tenha instalado os seguintes pré-requisitos em seu ambiente de desenvolvimento:

- [Java](https://www.java.com/en/download/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Github / repositório do projeto](https://github.com/wswilliams/springboot-restful-webservices.git) (para clonar o repositório do projeto springboot-restful-webservices)


## Configuração do Ambiente

Siga estas etapas para configurar o ambiente de desenvolvimento:

1. Clone o repositório do projeto (se você ainda não o fez):

2. Navegue até o diretório do projeto:


## Configuração do Docker

Siga estas etapas para configurar e executar os contêineres Docker:

1. Certifique-se de que o Docker e o Docker Compose estejam instalados e em execução.

2. Crie e inicie os contêineres usando o Docker Compose:

```sh
$ docker-compose up -d --build
```

3. Aguarde até que os contêineres estejam em execução. Você pode verificar o status dos contêineres com o seguinte comando:

```sh
$ docker ps
```

## Executando o Projeto

Agora que o ambiente está configurado e os contêineres estão em execução, você poderá acessar a aplicação:

1. Inicie o aplicativo Interface Swagger UI:

2. O aplicativo estará acessível em [http://localhost:8080/docs](http://localhost:8080/docs).

3. Faça upload dos dados para aplicação com os arquivos: data_1.txt, data_2.txt

4. Filtros por userId e (userId, startDate e endDate), ambas consulta utilzam cache no redis

## Verificar o cache no Redis

1. Comando line: docker exec -it CONTAINER_ID redis-cli -a password

```sh
$ docker exec -it CONTAINER_ID redis-cli -a eYVX7EwVmmxKPCDmwMtyKVge8oLd2t81
```
2. Comando line: Consultar o cache

```sh
$ KEYS *  "Lista todas os caches armazenados"
$ MGET key_item "Exibe os dados do cache especifico"
$ exit "Sair do redis"
```

## Encerrando o Projeto

Para encerrar a execução do projeto e desligar os contêineres Docker, execute o seguinte comando:
```sh
$ docker stop CONTAINER_ID
```

## Conclusão

O projeto springboot-restful-webservice está configurado e pronto para ser executado em seu ambiente de.
Certifique-se de seguir as etapas de configuração e execução conforme descrito acima.
