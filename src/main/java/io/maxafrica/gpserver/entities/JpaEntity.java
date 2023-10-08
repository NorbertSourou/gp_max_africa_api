package io.maxafrica.gpserver.entities;

import jakarta.persistence.Table;
import org.hibernate.annotations.*;

import java.lang.annotation.*;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SQLDelete(sql =
        "UPDATE {table} " +
                "SET deleted = true " +
                "WHERE id = ?")
@Table(name = "{table}")
@Where(clause = "deleted = false")
public @interface JpaEntity {
    String table();
}
