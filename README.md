# example-todo-rest-service


Postman Requests

GET /todo/ HTTP/1.1
Host: localhost:8080
Cache-Control: no-cache
Postman-Token: 5840f3b5-2a04-b057-b37b-039ae85cdfc0


GET /todo/37683a56-6604-4e24-adc5-69edf830d847 HTTP/1.1
Host: localhost:8080
Cache-Control: no-cache
Postman-Token: 72b1050c-d7bc-cf8e-46b3-fe64b46420b4

POST /todo HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: d5bef4f7-6a2c-9b2d-ebc3-0cd8ee634458

{"title":"title45", "description":"description45", "dueDate":"x2018-01-01T09:50:00"}

POST /todo/bulk HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 18604e4e-d9c7-bb61-6774-d131d69e7322

[
	{"title":"title55", "description":"description55", "dueDate":"2018-01-01T09:50:00"},
	{"title":"title599", "dueDate":"2018-01-01T09:50:00"},
	{"title":"title599", "description":"description57", "dueDate":"2018-01-01T09:50:00"}
]

PATCH /todo/37683a56-6604-4e24-adc5-69edf830d847 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 6be0065e-7268-d0ee-59d9-605e73d4efa4

{"title":"title1", "description":"description1", "dueDate":"2018-01-01T09:50:00"}
