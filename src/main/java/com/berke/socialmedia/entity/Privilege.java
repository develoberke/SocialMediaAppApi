package com.berke.socialmedia.entity;


import com.berke.socialmedia.entity.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

@Table(name = "privileges")

@SQLDelete(sql = "UPDATE privileges SET is_deleted=true WHERE id=?")
@Where(clause = "is_deleted=false")

public class Privilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

}
