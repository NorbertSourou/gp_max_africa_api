package io.maxafrica.gpserver.config;

import io.maxafrica.gpserver.entities.*;
import io.maxafrica.gpserver.entities.enums.TypePrivilege;
import io.maxafrica.gpserver.repositories.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoadConfig {

    private static final String COMMA_DELIMITER = ",";
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final PostRepository postRepository;

    @Value("${user.role}")
    private String userRole;


    @PostConstruct
    public void loadData() {
        loadRole();

        loadCategoriesCsv();
        loadSubCategories();
        loadPosts();

        loadTags();
        loadPostsTags();
    }

    private void loadPostsTags() {
    }

    private void loadTags() {

    }

    private void loadPosts() {
        if (postRepository.count() > 0) return;

        List<Post> postList = new ArrayList<>();

        Resource resource = new ClassPathResource("static/posts.csv");

        String filePath;

        try {
            filePath = resource.getFile().getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {

                    String[] values = line.split(COMMA_DELIMITER);

                    postList.add(new Post(values[1], values[2], values[4], values[0].replace("\"", ""), values[6]));
                    postRepository.save(new Post(values[1], values[2], values[4], values[0], values[6]));
                    log.info("Save posts " + values[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadLinkPostsCategories();
        loadPostsSubCategories();
    }


    private void loadLinkPostsCategories() {
        Resource resource = new ClassPathResource("static/category_post.csv");

        String filePath;

        try {
            filePath = resource.getFile().getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {

                    String[] values = line.split(COMMA_DELIMITER);
                    Optional<Post> postOptional = postRepository.findByPosition(values[2].replace("\"", ""));
                    if (postOptional.isPresent()) {
                        Post post = postOptional.get();
                        post.getCategories().add(categoryRepository.findByPosition(values[1].replace("\"", "")));
                        postRepository.save(post);
                        log.info("Add category to post " + post.getTitle());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadPostsSubCategories() {

        Resource resource = new ClassPathResource("static/post_subcategory.csv");

        String filePath;

        try {
            filePath = resource.getFile().getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {

                    String[] values = line.split(COMMA_DELIMITER);
                    Optional<Post> postOptional = postRepository.findByPosition(values[1]);
                    if (postOptional.isPresent()) {
                        Post post = postOptional.get();
                        post.getSubCategories().add(subCategoryRepository.findByPosition(values[2]));
                        postRepository.save(post);
                        log.info("Add subcategory to post" + subCategoryRepository.findByPosition(values[2]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadSubCategories() {
        if (subCategoryRepository.count() > 0) return;

        List<SubCategory> subCategoryList = new ArrayList<>();

        Resource resource = new ClassPathResource("static/subcategories.csv");
        String filePath;
        try {
            filePath = resource.getFile().getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMITER);
                    subCategoryList.add(new SubCategory(values[1], categoryRepository.findByPosition(values[3]), values[0]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        subCategoryRepository.saveAll(subCategoryList);
        log.info("Save all subcategories : " + subCategoryList.size());
    }


    public void loadRole() {
        if (roleRepository.count() > 0) return;

        List<Privilege> privilegeUser = new ArrayList<>();
        for (int i = 1; i < TypePrivilege.values().length; i++) {
            String description = TypePrivilege.values()[i].toString().replace("_", " ");
            privilegeUser.add(privilegeRepository.save(new Privilege(TypePrivilege.values()[i], description)));
        }

        Role roleUser = new Role(userRole, userRole);
        roleUser.setPrivileges(privilegeUser);
        roleRepository.save(roleUser);

    }

    public void loadCategoriesCsv() {
        if (categoryRepository.count() > 0) return;

        List<Category> categoriesList = new ArrayList<>();

        Resource resource = new ClassPathResource("static/categories.csv");

        String filePath;
        try {
            filePath = resource.getFile().getAbsolutePath();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMITER);
                    categoriesList.add(new Category(values[1], values[0]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.saveAll(categoriesList);
        log.info("Save all categories : " + categoriesList.size());
    }

}
