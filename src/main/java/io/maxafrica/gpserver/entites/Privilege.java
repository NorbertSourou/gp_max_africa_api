package io.maxafrica.gpserver.entites;

import io.maxafrica.gpserver.entites.enums.TypePrivilege;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "privileges")
@NoArgsConstructor
@SQLDelete(sql =
        "UPDATE privileges " +
                "SET deleted = true " +
                "WHERE id = ?")
@Where(clause = "deleted = false")
public class Privilege extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypePrivilege name;

    private String description;
}
