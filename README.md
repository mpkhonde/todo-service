|  Metod | Path                   | Beskrivning              | Svar                          |
| -----: | ---------------------- | ------------------------ | ----------------------------- |
|   POST | /api/todos             | Skapa todo (JSON body)   | 201 Created + `Location`      |
|   POST | /api/todos?title=Text  | Skapa todo (query-param) | 201 Created                   |
|    GET | /api/todos             | Lista alla               | 200 OK                        |
|    GET | /api/todos/{id}        | Hämta en                 | 200 / 404                     |
|    PUT | /api/todos/{id}        | Uppdatera en todo        | 200 / 404                     |
|  PATCH | /api/todos/{id}/toggle | Markera som klar/oklar   | 200 / 404                     |
| DELETE | /api/todos/{id}        | Radera en                | 204 / 404                     |
| DELETE | /api/todos?ids=1,2,3   | Radera många (query)     | 200 `{deleted[], notFound[]}` |
| DELETE | /api/todos/bulk/1,2,3  | Radera många (path)      | 200 `{deleted[], notFound[]}` |
| DELETE | /api/todos             | Radera alla              | 204                           |


# Skapa (JSON)
curl -i -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Första"}'

# Skapa (query-param)
curl -i -X POST "http://localhost:8080/api/todos?title=Andra"

# Lista alla
curl -s http://localhost:8080/api/todos | jq

# Hämta en
curl -i http://localhost:8080/api/todos/1

# Uppdatera en
curl -i -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Uppdaterad titel","done":true}'

# Markera klar/oklar
curl -i -X PATCH http://localhost:8080/api/todos/1/toggle

# Radera en
curl -i -X DELETE http://localhost:8080/api/todos/1

# Bulk-radera (query)
curl -s -X DELETE "http://localhost:8080/api/todos?ids=4,5,6"

# Bulk-radera (path)
curl -s -X DELETE "http://localhost:8080/api/todos/bulk/7,8"

# Radera alla
curl -i -X DELETE http://localhost:8080/api/todos
