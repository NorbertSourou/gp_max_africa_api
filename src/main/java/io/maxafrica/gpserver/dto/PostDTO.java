package io.maxafrica.gpserver.dto;

import io.maxafrica.gpserver.entities.Tag;
import io.maxafrica.gpserver.entities.User;
import io.maxafrica.gpserver.entities.enums.PostLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private UUID id;

    @NotBlank(message = "title may not be null")
    private String title;

    private String description;

    @NotBlank(message = "Url may not be null")
    private String url;

    private String position;

    @NotBlank(message = "imageUrl may not be null")
    private String imageUrl;

    private Long nbLikes;

    private Long nbComments;

    private Long nbViews;

    private PostLevel postLevel;

    private User user;

    private List<Tag> tags;

}
