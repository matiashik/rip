---
marp: true
theme: gaia
math: mathjax
---

# Сервис LaTeX выражений

- Кодинцев М.А.
- Гадалина А.А.
- Пучкова Е.А.

---

## Основная функция

На входе `\frac{1}{2}`

На выходе

$$
\frac{1}{2}
$$

Точнее

```base64
R0lGODlhCQAiALMAAP///wAAANbW1kZGRgQEBHR0dDQ0NFxcXJKSkiQkJA4ODhgYGLCwsAAAAAAAAAAAACH5BAEAAAAALAAAAAAJACIAA
ARREAAxiLxlmHD74Z30hSIYjiR6mh57qRJiEEFikHiu73wY/EBgb0jMJRKKheCilCxAggBCwggUJoGDJDpIBRgkQoKUWKRuoQwmtii4DdNoEBQBADs=
```

---

## User

- `POST /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `DELETE /users/{id}`

```json
{
    "id": 1,
    "login": "superLog",
    "password": "superPass"
}
```

---

## Session

- `POST /users/{user_id}/sessions`
- `GET /users/{user_id}/sessions`
- `DELETE /users/{user_id}/sessions/{id}`

```json
[
 {
  "id": 1,
  "token": "1-Av_VzrBRRKIbtoG_ilOXSQEcgJTCkltyomRHr27PoH-TnJlgM8KD_eI
  RcMoEGBfrs5CTffERAIJrmBIuMAfEINd3HLRbROf8Y7Ug41sQoVZcYpJSLs0o-8e
  OHKkapYeYBf1v5_m8Tf6s90y2pPy1KwNby__GKmX1OVRGGAmgSQI"
 }
]
```

---

## Expression

- `POST /expressions`
- `GET /expressions`
- `GET /expressions/{id}`
- `PUT /expressions/{id}`
- `DELETE /expressions/{id}`

```json
{
 "id": 1,
 "latex": "\\frac{1}{2}",
 "expressionImage": {
  "id": 1,
  "content": "R0lGODlhCQAiALMAAP///wAAANbW1kZGRgQEBHR0dDQ0NFxcXJKSkiQkJA4ODhgYGLCwsAAAAAAAAAAAACH5BAEAAAAALAAAAAAJACIAAARREAAxiLxlmHD74
  Z30hSIYjiR6mh57qRJiEEFikHiu73wY/EBgb0jMJRKKheCilCxAggBCwggUJoGDJDpIBRgkQoKUWKRuoQwmtii4DdNoEBQBADs=",
  "published": false
 }
}
```

---

### Expression image

- `GET /expressions/{id}/image`
- `PATCH /expressions/{id}/image/publish`
- `PATCH /expressions/{id}/image/unpublish`

```base64
R0lGODlhCQAiALMAAP///wAAANbW1kZGRgQEBHR0dDQ0NFxcXJKSkiQkJA4ODhgYGLCwsAAAAAAAAAAAACH5BAEAAAAALAAAAAAJACIAA
ARREAAxiLxlmHD74Z30hSIYjiR6mh57qRJiEEFikHiu73wY/EBgb0jMJRKKheCilCxAggBCwggUJoGDJDpIBRgkQoKUWKRuoQwmtii4DdNoEBQBADs=
```

---

## Errors

- `ValidationException` - 422
- `AuthorizationException` - 403
- `AuthenticationException` - 401
- `NoSuchElementException` - 404

```json
{
 "error": "codes [id.not_authenticated];"
}
```

---

## Модные фишки

- `REST`
- `Controller` + `Service` + `Validator` + `Repository` + `Entity` + `liquid` миграция на каждый ресурс
- `Lombok` и `Hibernate`
- `javadoc`
