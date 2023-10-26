package io.maxafrica.gpserver.config;

import io.maxafrica.gpserver.entities.*;
import io.maxafrica.gpserver.entities.enums.TypePrivilege;
import io.maxafrica.gpserver.repositories.*;
import jakarta.annotation.PostConstruct;
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

@Configuration
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

    public DataLoadConfig(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository, PostRepository postRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;

        this.postRepository = postRepository;
    }

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
                    System.out.println("Save posts " + values[1]);
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
                    Post post = postRepository.findByPosition(values[2]);
                    if (post != null) {
                        post.setCategory(categoryRepository.findByPosition(values[1]));

                        postRepository.save(post);

                        System.out.println("Save category to post" + post.getTitle());

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
                    Post post = postRepository.findByPosition(values[1]);
                    if (post != null) {
                        post.getSubCategory().add(subCategoryRepository.findByPosition(values[2]));
                        postRepository.save(post);

                        System.out.println("Save subcategory to post" + subCategoryRepository.findByPosition(values[2]));
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
                    System.out.println("Save subcategory " + values[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        subCategoryRepository.saveAll(subCategoryList);
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
                    System.out.println("Save category " + values[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.saveAll(categoriesList);
    }

}
