package com.berke.socialmedia.dao.entity;


import com.berke.socialmedia.dao.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})

@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id =?")
@Where(clause = "is_deleted=false")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> user;

    @ManyToMany
    @JoinTable(name = "roles_privileges",
    joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "privilege_id",referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();

    public void addPrivilege(Privilege privilege){
        this.privileges.add(privilege);
    }

    public void removePrivilege(Privilege privilege){
        this.privileges.remove(privilege);
    }

}
