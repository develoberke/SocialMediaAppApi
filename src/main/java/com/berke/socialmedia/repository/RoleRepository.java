package com.berke.socialmedia.repository;

import com.berke.socialmedia.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);
}
