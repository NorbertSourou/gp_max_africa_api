package io.maxafrica.gpserver.entities;

import io.maxafrica.gpserver.entities.enums.PostFeedback;
import io.maxafrica.gpserver.entities.enums.PostLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Types;
import java.util.UUID;

@Data
@Entity
@Table(name = "feedbacks")
@SQLDelete(sql =
        "UPDATE feedbacks " +
                "SET deleted = true " +
                "WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @JdbcTypeCode(Types.VARCHAR)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PostFeedback postFeedback;

    @ManyToOne
    Post post;

    @ManyToOne
    User user;
}
