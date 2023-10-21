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

    private String position;

    @NotBlank
    private String imageUrl;

    private Long nbLikes;

    private Long nbComments;

    private Long nbViews;

    public Post(String title, String description, String url, String position, String imageUrl) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.position = position;
        this.imageUrl = imageUrl;
    }

    @Column(columnDefinition = "varchar(50) default 'PUBLIC'")
    @Enumerated(EnumType.STRING)
    private PostLevel postLevel;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<SubCategory> subCategory;

    @ManyToMany
    private List<Tag> tags;

}
