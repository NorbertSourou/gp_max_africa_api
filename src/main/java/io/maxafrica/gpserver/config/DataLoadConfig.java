package io.maxafrica.gpserver.config;

import io.maxafrica.gpserver.entities.*;
import io.maxafrica.gpserver.entities.enums.TypePrivilege;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final EntityManager entityManager;

    @Value("${user.role}")
    private String userRole;


    @PostConstruct
    public void loadData() {
        loadRole();
        addActuatorUser();

        loadCategoriesCsv();
        loadSubCategories();
        loadPosts();

        loadTags();
        loadPostsTags();
    }

    public void loadRole() {
        if (roleRepository.count() > 0) return;

        List<Privilege> privilegeUser = new ArrayList<>();
        for (int i = 1; i < TypePrivilege.values().length; i++) {
            String description = TypePrivilege.values()[i].toString().replace("_", " ");
            privilegeUser.add(new Privilege(TypePrivilege.values()[i], description));
        }
        Role roleUser = new Role(userRole, userRole);
        roleUser.setPrivileges(privilegeUser);
        roleRepository.save(roleUser);


        Privilege privilege = new Privilege(TypePrivilege.ACTUATOR, "ACTUATOR");
//        privilege = privilegeRepository.save(privilege);

        List<Privilege> privilegeActuator = new ArrayList<>();
        privilegeActuator.add(privilege);

        Role role = new Role("ACTUATOR", "ACTUATOR");
        role.setPrivileges(privilegeActuator);
        roleRepository.save(role);
    }

    private void addActuatorUser() {
        if (userRepository.existsByUsername("actuator")){
            return;
        }

        Role role = roleRepository.findByName("ACTUATOR").orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ACTUATOR"));
        User user = new User();
        user.setUsername("actuator");
        user.setEmail("admin.actuator@maxafrica.io");
        user.setPassword(passwordEncoder.encode("actuatorPWD"));
        user.setRole(role);
        userRepository.save(user);
    }

    private void loadPostsTags() {
    }

    private void loadTags() {

    }

    private void loadPosts() {
        if (postRepository.count() > 0) return;
        try(InputStream inputStream = getClass().getResourceAsStream("/static/posts.csv")) {
            try {
                assert inputStream != null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {

                        String[] values = line.split(COMMA_DELIMITER);
                        postRepository.save(new Post(values[1], values[2], values[4], values[0], values[6]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("Save all posts " + postRepository.count());

        loadLinkPostsCategories();
        loadPostsSubCategories();
    }


    private void loadLinkPostsCategories() {
        try(InputStream inputStream = getClass().getResourceAsStream("/static/category_post.csv")) {
            try {
                assert inputStream != null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {

                        String[] values = line.split(COMMA_DELIMITER);
                        Optional<Post> postOptional = postRepository.findByPosition(values[2].replace("\"", ""));
                        if (postOptional.isPresent()) {
                            Post post = postOptional.get();
                            post.getCategories().add(categoryRepository.findByPosition(values[1].replace("\"", "")));
                            postRepository.save(post);
                        }
                    }
                    log.info("Set  category to each post");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadPostsSubCategories() {
        try(InputStream inputStream = getClass().getResourceAsStream("/static/post_subcategory.csv")) {
            try {
                assert inputStream != null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {

                        String[] values = line.split(COMMA_DELIMITER);
                        Optional<Post> postOptional = postRepository.findByPosition(values[1]);
                        if (postOptional.isPresent()) {
                            Post post = postOptional.get();
                            post.getSubCategories().add(subCategoryRepository.findByPosition(values[2]));
                            postRepository.save(post);
                        }
                    }

                    log.info("Set subcategory to each post");
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

        try(InputStream inputStream = getClass().getResourceAsStream("/static/subcategories.csv")) {
            try {
                assert inputStream != null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                    br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] values = line.split(COMMA_DELIMITER);
                        subCategoryList.add(new SubCategory(values[1], categoryRepository.findByPosition(values[3]), values[0]));
                    }
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

    public void loadCategoriesCsv() {
        if (categoryRepository.count() > 0) return;

        List<Category> categoriesList = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream("/static/categories.csv")) {
            assert inputStream != null;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                br.readLine(); // Skip header if needed
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMITER);
                    categoriesList.add(new Category(values[1], values[0]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryRepository.saveAll(categoriesList);
        log.info("Save all categories : " + categoriesList.size());
    }

}
