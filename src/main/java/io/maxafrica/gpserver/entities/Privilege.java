package io.maxafrica.gpserver.entities;

import io.maxafrica.gpserver.entities.enums.TypePrivilege;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "privileges")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql =
        "UPDATE privileges " +
                "SET deleted = true " +
                "WHERE id = ?")
@Where(clause = "deleted = false")
@Data
public class Privilege extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypePrivilege name;

    private String description;
}
