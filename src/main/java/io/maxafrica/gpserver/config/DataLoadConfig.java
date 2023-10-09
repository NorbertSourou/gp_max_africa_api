package io.maxafrica.gpserver.config;

import io.maxafrica.gpserver.entities.Privilege;
import io.maxafrica.gpserver.entities.Role;
import io.maxafrica.gpserver.entities.enums.TypePrivilege;
import io.maxafrica.gpserver.repositories.PrivilegeRepository;
import io.maxafrica.gpserver.repositories.RoleRepository;
import io.maxafrica.gpserver.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoadConfig {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${user.role}")
    private String userRole;

    public DataLoadConfig(RoleRepository roleRepository, PrivilegeRepository privilegeRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void loadData() {
        loadRole();
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
}
