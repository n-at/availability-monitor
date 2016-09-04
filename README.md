availability-monitor
====================

Check sites availability. Send email on availability status change.

Build
=====

JDK >= 8 required.

    $ ./mvnw clean package
    
Run
===

In `application.yml` set up email server settings (see example in `application.sample.yml`).

Run packaged version from `target` directory. Or with Spring Boot maven plugin:
 
    $ ./mvnw spring-boot:run

Open [http://localhost:8080](http://localhost:8080) in browser.

License
=======

BSD, see `LICENSE`
