package com.berke.socialmedia.dao.entity;

import com.berke.socialmedia.dao.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "profiles")

@SQLDelete(sql = "UPDATE profiles SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Profile extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "picture")
    private byte[] picture;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "level")
    private Long level = 1L;

    @ManyToMany
    @JoinTable(name = "following_followers", joinColumns = @JoinColumn(name = "following_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "followers_id", referencedColumnName = "id"))
    private Set<Profile> following;

    @ManyToMany(mappedBy = "following")
    private Set<Profile> followers;


    @OneToMany(mappedBy = "profile")
    private Set<Post> posts;


    private void addFollower(Profile profile){
        if(profile != null && profile != this) {
            this.followers.add(profile);
        }
    }

    private void removeFollower(Profile profile){
        if(profile != null && profile != this){
            this.followers.remove(profile);
        }
    }

    public void addFollowing(Profile profile){
        if(profile != null && profile != this){
            this.following.add(profile);
            profile.addFollower(this);
        }
    }

    public void removeFollowing(Profile profile){
        if(profile != null && profile != this){
            this.following.remove(profile);
            profile.removeFollower(this);
        }
    }

    public void addPost(Post post){
        if(post != null){
            this.posts.add(post);
            post.setProfile(this);
        }
    }

    public void removePost(Post post){
        if(post != null) {
            this.posts.remove(post);
            post.setProfile(null);
        }
    }
    //add follower fonksiyonları eklenecek
    // örnek https://github.com/nhabbash/tanuki/blob/master/src/main/java/com/web/tanuki/model/TanukiUser.java
}
