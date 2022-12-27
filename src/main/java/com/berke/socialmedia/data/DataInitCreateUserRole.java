package com.berke.socialmedia.data;

import com.berke.socialmedia.repository.PrivilegeRepository;
import com.berke.socialmedia.repository.ProfileRepository;
import com.berke.socialmedia.repository.RoleRepository;
import com.berke.socialmedia.repository.UserRepository;
import com.berke.socialmedia.entity.Privilege;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.entity.Role;
import com.berke.socialmedia.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@Order(1)
public class DataInitCreateUserRole implements CommandLineRunner {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void run(String... args) throws Exception {


        Privilege readPrivilege = createPrivilege("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilege("WRITE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilege("DELETE_PRIVILEGE");

        Role adminRole = createRole("ROLE_ADMIN", Set.of(readPrivilege,writePrivilege,deletePrivilege));
        Role memberRole = createRole("ROLE_MEMBER", Set.of(readPrivilege,writePrivilege,deletePrivilege));



        User dbAdminUser = userRepository.findByUsername("admin");
        User dbMemberUser = userRepository.findByUsername("member");
        User dbBothUser = userRepository.findByUsername("both");
        User dbNoneUser = userRepository.findByUsername("none");

        Profile profile1 = Profile.builder().firstName("Berke").lastName("Yıldırım").level(1L).build();
        Profile profile2 = Profile.builder().firstName("Berke2").lastName("Yıldırım").level(1L).build();
        Profile profile3 = Profile.builder().firstName("Berke3").lastName("Yıldırım").level(1L).build();
        Profile profile4 = Profile.builder().firstName("Berke4").lastName("Yıldırım").level(1L).build();

        if (dbAdminUser==null) {
            Collection<Role> myroles = new ArrayList<Role>();
            User adminuser = User.builder()
                    .username("admin")
                    .password(bCryptPasswordEncoder.encode("password"))
                    .email("admin@berke.xyz")
                    .roles(Set.of(adminRole))
                    .profile(profile2)
                    .build();
            profile2.setUser(adminuser);
            profileRepository.save(profile2);
            userRepository.save(adminuser);
        }

        if (dbMemberUser==null) {
            User memberUser = User.builder()
                    .username("member")
                    .password(bCryptPasswordEncoder.encode("password"))
                    .email("member@berke.xyz")
                    .roles(Set.of(memberRole))
                    .profile(profile1)
                    .build();
            profile1.setUser(memberUser);
            profileRepository.save(profile1);
            userRepository.save(memberUser);
        }

        if (dbBothUser==null) {
            Set<Role> myroles = new HashSet<>();
            myroles.add(adminRole);
            myroles.add(memberRole);
            User bothUser = User.builder()
                    .username("both")
                    .password(bCryptPasswordEncoder.encode("password"))
                    .email("both@berke.xyz")
                    .roles(myroles)
                    .profile(profile3)
                    .build();
            profile3.setUser(bothUser);
            profileRepository.save(profile3);
            userRepository.save(bothUser);
        }

        if (dbNoneUser==null) {
            Set<Role> myroles = new HashSet<>();
            User noneRoleUser = User.builder()
                    .username("noneRole")
                    .password(bCryptPasswordEncoder.encode("password"))
                    .email("noneRole@berke.xyz")
                    .roles(myroles)
                    .profile(profile4)
                    .build();
            profile4.setUser(noneRoleUser);
            profileRepository.save(profile4);
            userRepository.save(noneRoleUser);
        }


    /*
        GenericSpecification<Privilege> privilegeGenericSpecification = new GenericSpecification<Privilege>();
        privilegeGenericSpecification.add(new SearchCriteria("name","PRIVILEGE",SearchOperation.MATCH));
        List<Privilege> privilegeList = privilegeRepository.findAll(privilegeGenericSpecification);
        privilegeList.forEach(privilege -> {
            System.out.println(privilege.getName());
        });



        Project projectToSave = Project.builder().name("Project nameExecution").status(ProjectStatus.Execution)
                .build();
        projectRepository.save(projectToSave);
        GenericSpecification projectSpecification = new GenericSpecification<Project>();
        projectSpecification.add(new SearchCriteria("status", ProjectStatus.Execution, SearchOperation.EQUAL));
        //genericSpecification.add(new SearchCriteria("name", "Project nameInitiation",SearchOperation.EQUAL));
        //projectSpecification.add(new SearchCriteria("name", "Project", SearchOperation.MATCH));
        List<Project> projectList = projectRepository.findAll(projectSpecification);
        projectList.forEach(project -> {
            System.out.println(project.getName());
        });

     */
    }

    private Privilege createPrivilege(String name){
        Privilege privilege = privilegeRepository.findByName(name);
        if(privilege == null){
             privilege = Privilege.builder()
                    .name(name)
                    .description(name + " Privilege")
                    .build();
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
    private Role createRole(
            String name, Set<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = Role.builder()
                    .name(name)
                    .description(name + " role")
                    .build();
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
