package io.maxafrica.gpserver.repositories;

import io.maxafrica.gpserver.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
