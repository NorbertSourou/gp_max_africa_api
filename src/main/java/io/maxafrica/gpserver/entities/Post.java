package io.maxafrica.gpserver.entities;

import io.maxafrica.gpserver.entities.enums.PostLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
@SQLDelete(sql =
        "UPDATE posts " +
                "SET deleted = true " +
                "WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String url;

    @NotBlank
    private String imageUrl;

    private Long nbLikes;

    private Long nbComments;

    private Long nbViews;

    @Column(columnDefinition = "varchar(50) default 'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private PostLevel postLevel;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    @ManyToMany
    private List<SubCategory> subCategory;

    @ManyToMany
    private List<Tag> tags;

}
