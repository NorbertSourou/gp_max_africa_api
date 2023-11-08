package io.maxafrica.gpserver.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostData {
    private List<UUID> categoriesIds = new ArrayList<>();
    private List<Long> subCategoriesIds = new ArrayList<>();
    private List<Long> tagIds = new ArrayList<>();
    @Valid
    private PostDTO postDto;
}
