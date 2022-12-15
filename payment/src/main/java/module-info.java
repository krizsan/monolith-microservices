module payment {
    requires lombok;
    requires jakarta.persistence;
    requires jakarta.transaction;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.data.jpa;

    exports se.ivankrizsan.monolithmicroservices.payment.api;
    exports se.ivankrizsan.monolithmicroservices.payment.configuration;

    opens se.ivankrizsan.monolithmicroservices.payment.configuration to spring.core;
    opens se.ivankrizsan.monolithmicroservices.payment.implementation to spring.core;
}