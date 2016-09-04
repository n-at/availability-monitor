availability-monitor
====================

Check sites availability. Send email on availability status change.

Build
=====

JDK >= 8 required.

    $ ./mvnw clean package
    
Run
===

Run packaged version from `target` directory. Or with Spring Boot maven plugin:
 
    $ ./mvnw spring-boot:run

In `application.yml` set up email server settings (see example in `application.sample.yml`).

License
=======

BSD, see `LICENSE`
