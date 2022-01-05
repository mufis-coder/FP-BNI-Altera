# Final Projek - Bootcamp Altera-BNI

Ini adalah repository untuk menyimpan tugas final projek dari bootcamp yang diadakan oleh BNI bersama Altera. FP ini menggunakan arsitektur microservice dengan framework java spring-boot. Berikut link mvp produk yang dikerjakan pada FP ini: https://docs.google.com/presentation/d/1DLc0_9IllTVPodi6QW0PwkO3uWzIgfHyhlHCfxVwXqk/edit?usp=sharing

## Diagram ERD

Berikut adalah gambar diagram ERD dari projek fp ini:

![alt text](https://github.com/mufis-coder/FP-BNI-Altera/blob/master/src/img/ERD-FP-BNI.jpg) <br />

## High Level Architecture

Berikut adalah gambar dari High Level Architecture dari projek fp ini:

![alt text](https://github.com/mufis-coder/FP-BNI-Altera/blob/master/src/img/high-level-arsitektur.jpg) <br />

## Link dan port

Berikut adalah link dan port yang digunakan pada service-service yang ada pada projek fp ini:

    - Link swagger: http://localhost:9000/swagger-ui/
    - api-gateway: http://localhost:9000/
    - eureka: http://localhost:8761/
    - kafka: http://localhost:9092/
    - auth-service: http://localhost:8081/
    - post-service: http://localhost:8083/
    - log-service: http://localhost:8085/
    - category-service: http://localhost:8087/

## Cara Run aplikasi Final-Projek

- Dalam satu IntelliJ-IDEA, pada path discovery-service:

    a. run spring aplikasi discovery-service

    b. run kafka: https://github.com/mufis-coder/tips-tricks-code/tree/main/Kafka

        - run zookeeper
        - run server-kafka
        - run create-topic
        - run read-topic

- Buka wsl-ubuntu

    a. run postgresql-ubuntu, tutorial lebih lengkap di: https://github.com/mufis-coder/tips-tricks-code/tree/main/Posgresql/Postgresql-Ubuntu

- Buka MongoDBCompass

    a. Karena mongodb menggunakan remote server, maka tidak perlu menggunakan wsl-ubuntu (untuk runnya)

- Buka IntelliJ-IDEA untuk setiap service yang ada dan run menggunakan IntelliJ-IDEA tersebut.

    a. discovery-service

    b. api-gateway

    c. auth-service

    d. post-service

    e. log-service

    f. category-service