# Todo Service

En enkel **REST-baserad webbtjänst** för att hantera *todos*.  
Byggd med **Spring Boot 3**, **Java 17**, **PostgreSQL 16** och körbar via **Docker Compose**.

---

## Funktionalitet
- [x] Skapa en todo (JSON-body eller query-param)
- [x] Lista alla todos
- [x] Hämta en todo via ID
- [x] Uppdatera en todo
- [x] Markera en todo som klar/oklar
- [x] Radera en todo
- [x] Radera flera todos (query eller path)
- [x] Radera alla todos

---

## Endpoints

| Metod   | Path                    | Beskrivning                  | Svar                           |
|---------|-------------------------|------------------------------|--------------------------------|
| POST    | `/api/todos`            | Skapa todo (JSON body)       | 201 Created + Location         |
| POST    | `/api/todos?title=Text` | Skapa todo (query-param)     | 201 Created                    |
| GET     | `/api/todos`            | Lista alla                   | 200 OK                         |
| GET     | `/api/todos/{id}`       | Hämta en                     | 200 OK / 404 Not Found         |
| PUT     | `/api/todos/{id}`       | Uppdatera en                 | 200 OK / 404 Not Found         |
| PATCH   | `/api/todos/{id}/toggle`| Markera klar/oklar           | 200 OK / 404 Not Found         |
| DELETE  | `/api/todos/{id}`       | Radera en                    | 204 No Content / 404 Not Found |
| DELETE  | `/api/todos?ids=1,2,3`  | Radera många (query)         | 200 `{deleted[], notFound[]}`  |
| DELETE  | `/api/todos/bulk/1,2,3` | Radera många (path)          | 200 `{deleted[], notFound[]}`  |
| DELETE  | `/api/todos`            | Radera alla                  | 204 No Content                 |

---

## CURL

```bash
# Skapa todo (JSON body)
curl -i -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Första"}'

# Skapa todo (query-param)
curl -i -X POST "http://localhost:8080/api/todos?title=Andra"

# Lista alla
curl -i http://localhost:8080/api/todos | jq

# Hämta en
curl -i http://localhost:8080/api/todos/1

# Uppdatera en
curl -i -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Uppdaterad todo"}'

# Markera som klar/oklar
curl -i -X PATCH http://localhost:8080/api/todos/1/toggle

# Radera en
curl -i -X DELETE http://localhost:8080/api/todos/1

# Bulk-radera (query)
curl -s -X DELETE "http://localhost:8080/api/todos?ids=4,5,6"

# Bulk-radera (path)
curl -s -X DELETE "http://localhost:8080/api/todos/bulk/7,8"

# Radera alla
curl -i -X DELETE http://localhost:8080/api/todos


---

## Test
Detta är en teständring för squash-merge.

-------------------------------------------------------------------------------------------------

Kom igång

1. Klona repo
git clone https://github.com/mpkhonde/todo-service.git
cd todo-service

-------------------------------------------------------------------------------------------------
2. Bygg & starta med Docker
docker-compose down -v   # stoppa och ta bort gamla containers
docker-compose up --build

