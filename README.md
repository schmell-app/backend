# Schmell Backend
This is the backend for the schmell application and admin panel. It is written in Kotlin and uses the Spring Boot framework.
All logic is handled by the backend, the frontend is only used to display the data.

## Technologies used
- Kotlin
- Spring Boot
- Spring Security
- Spring Data JPA
- AWS S3
- SendGrid
- PostgreSQL
- Auth0
- Docker

## Getting Started

### Prerequisites
- Java 11
- Docker
- Docker Compose
- Gradle
- An IDE (IntelliJ IDEA recommended)
- Postman (optional)
- PostgreSQL (optional)

### Local Database 
We use a PostgreSQL database for local development. You can either install PostgreSQL locally or use Docker to run a container with PostgreSQL.
To run a container with PostgreSQL, run the following command in the root directory of the project:
```docker-compose up -d```

### Installing and Running
1. Clone the repository:
    ```bash
    git clone https://github.com/schmell-app/backend.git
    ```

2. Create an ```application.yml``` file in the ```src/main/resources``` directory. The file should contain all the properties listed in the ```application-sample.yml``` file in the root directory.

3. Install necessary dependencies with Gradle:
    ```bash
    ./gradlew build
    ```

4. Run the project:
    ```bash
    ./gradlew bootRun
    ```

The project should now be running on ```localhost:8080```

## License
This work is licensed under [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa]. See `LICENSE` for more information.


[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/