package com.berke.socialmedia.dao;

import com.berke.socialmedia.dao.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


@Transactional
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {

    Privilege findByName(String name);
}
