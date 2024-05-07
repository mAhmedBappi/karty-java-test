# karty-java-test

Karty Java Developer Assignment

## To run the project

- Make sure docker is installed and running on the system
- Clone the Repository
- `cd` to the docker folder
- run `docker-compose up -d`

## Usage

- The login API returns JWT Token in the response header
- This token is used in the Products APIs auth-header's Bearer Token field

## Technology Stack

- Java 17, Spring Boot
- MySql - DB
- Redis - Cache
- JWT Token and Spring Security

## Features

- **Products** CRUD with soft deletion, caching for read, JWT Tokens for authentication. The reads are from cache, on write both DB and Cache are updated. Cache miss mechanism is implemented for single product read. 
- **User** signup, login, deletion is implemented. A user can signup and login to get JWT token which can then be used access Product APIs.
- **JWT Tokens** Stateless authentication using JWT Tokens. Username and Password are used to generate the JWT Tokens. JWT tokens have an expiry time. Each login generates a new token for the same user.
- **Central logging** for API requests and responses
- **Central Exception / Error Response** handling for Rest APIs
- **Rate Limiting** for Product API
- **Products Search** by name and description. As this is a bulk (name, description are not unique) read operation, the queries are made to the cache.

## Issues and Improvements

- Unit Test are not added
- JWT Authentication failure exceptions' stack trace is shown in the logs. Although the functionality is working fine. This is coming from the JWT Filter layer which is why the Rest Controller central Exception handler is unable to catch these exception.
- Redis read queries can be optimized, especially in the new Redis versions which now allow native JSON queries.