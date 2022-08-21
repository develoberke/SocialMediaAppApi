package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);
}
