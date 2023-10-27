package io.maxafrica.gpserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.maxafrica.gpserver.entities.enums.PostLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
@Builder
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

    private String position;

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<SubCategory> subCategories = new HashSet<>();

    @ManyToMany
    private List<Tag> tags;

    public Post(String title, String description, String url, String position, String imageUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.position = position;
        this.imageUrl = imageUrl;
    }
}
