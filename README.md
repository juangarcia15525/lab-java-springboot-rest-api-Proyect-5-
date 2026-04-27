# Spring Boot REST API Lab 05 Final

Aplicacion REST desarrollada con Spring Boot para gestionar productos y clientes en memoria.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Boot DevTools
- Spring Boot Starter Validation
- Maven

## Requisitos

- API-Key obligatoria en todas las peticiones: `123456`
- Datos validados en productos y clientes
- Manejo global de errores con respuestas JSON

## Estructura

- `model`: clases `Product` y `Customer`
- `service`: logica de negocio en memoria
- `controller`: endpoints REST
- `config`: interceptor de API key
- `exception`: excepciones personalizadas y handler global

## Endpoints

### Products

- `POST /products`
- `GET /products`
- `GET /products/{name}`
- `PUT /products/{name}`
- `DELETE /products/{name}`
- `GET /products/category/{category}`
- `GET /products/price?min={min}&max={max}`

### Customers

- `POST /customers`
- `GET /customers`
- `GET /customers/{email}`
- `PUT /customers/{email}`
- `DELETE /customers/{email}`

## Ejemplo de headers

```http
API-Key: 123456
Content-Type: application/json
```

## Ejemplo de payloads

### Product

```json
{
  "name": "Laptop",
  "price": 1200,
  "category": "Tech",
  "quantity": 5
}
```

### Customer

```json
{
  "name": "Ana",
  "email": "ana@example.com",
  "age": 30,
  "address": "Madrid"
}
```

## Ejecutar el proyecto

```bash
mvn spring-boot:run
```

La API queda disponible en:

```text
http://localhost:8080
```

## Ejecutar tests

```bash
mvn test
```

## Validaciones implementadas

### Product

- `name`: obligatorio, minimo 3 caracteres
- `price`: numero positivo
- `category`: obligatoria
- `quantity`: numero positivo

### Customer

- `name`: obligatorio
- `email`: formato valido
- `age`: minimo 18
- `address`: obligatoria

## Manejo de errores

La API devuelve respuestas con codigo HTTP adecuado para:

- errores de validacion
- API-Key ausente
- API-Key invalida
- producto no encontrado
- cliente no encontrado
- rango de precios invalido
- parametros faltantes o con tipo invalido

## Verificacion realizada

- La aplicacion compila correctamente
- Los tests pasan correctamente
- Se verificaron endpoints clave en `localhost:8080`
