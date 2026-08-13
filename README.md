# Yora

## What is Yora?
Yora is an expense tracking application allowing users to upload pictures of their receipts,
which are then analyzed to extract the information given.
This reduces the need to manually type in any expenses, making it easier to track overall spending.

## Structure
Yora consists of three main components, the backend, the frontend and the worker.

### Backend
The backend is written in Kotlin using the Spring Framework and a REST-API.
Metadata of uploaded files is stored in a PostgreSQL database, and a messeage is published via RabbitMQ, notifying the Python worker about new jobs.
TODO: S3 storage for images

### Python Worker
A python worker consumes the message and processes the uploaded image. 
Currently experimenting with ocr and a locally hosted LLM.

### Frontend
Not yet decided, but the idea is to build a mobile app and/or a web interface


## Run
To run the backend, simply clone the repository, run `docker compose up` and run YoraBackendApplication.
You can try out the current API by navigating to http://localhost:8080/swagger-ui/index.html