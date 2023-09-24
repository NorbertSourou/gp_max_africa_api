package io.maxafrica.gpserver.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Collection;

@Entity
@Table(name = "roles")
@SQLDelete(sql =
        "UPDATE roles " +
                "SET deleted = true " +
                "WHERE id = ?")
@Where(clause = "deleted = false")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @JsonIgnore
    private Collection<Privilege> privileges;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @JsonIgnore
    private User createdBy;
}
