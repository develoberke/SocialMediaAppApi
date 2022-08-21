package com.berke.socialmedia.service.impl;

import com.berke.socialmedia.dao.ProfileRepository;
import com.berke.socialmedia.dao.RoleRepository;
import com.berke.socialmedia.dao.UserRepository;
import com.berke.socialmedia.dao.entity.Privilege;
import com.berke.socialmedia.dao.entity.Profile;
import com.berke.socialmedia.dao.entity.Role;
import com.berke.socialmedia.dao.entity.User;
import com.berke.socialmedia.dto.UserDto;
import com.berke.socialmedia.dto.UserRegisterDto;
import com.berke.socialmedia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  ProfileRepository profileRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
        }
        throw new UsernameNotFoundException("Invalid username or password");
    }

    private Set<SimpleGrantedAuthority> getAuthorities(Collection<Role> roles){
        List<String> privileges = getPrivileges(roles);
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        privileges.forEach(privilege -> {
            authorities.add(new SimpleGrantedAuthority(privilege));
        });
        return authorities;
    }

    private List<String> getPrivileges(Collection<Role> roles){
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection =new ArrayList<>();

        for(Role role : roles){
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege privilege : collection){
            privileges.add(privilege.getName());
        }
        return privileges;
    }

    @Override
    public List<UserDto> getAll() {
        return Arrays.asList(modelMapper.map(userRepository.findAll(),UserDto[].class));
    }

    @Override
    public UserDto getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return modelMapper.map(user.get(),UserDto.class);
        }
        throw new EntityNotFoundException("There is no user with this id");
    }

    @Override
    public UserDto getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null)
            return modelMapper.map(user, UserDto.class);
        throw new EntityNotFoundException("User not found with this username");
    }

    @Override
    public UserDto save(UserDto userDto) {
        Optional<User> control = userRepository.findById(userDto.getId());
        if(control.isPresent()){
            throw new IllegalArgumentException("Id already exist");
        }
        User user = modelMapper.map(userDto,User.class);
        user = userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        User user = new User();
        Profile profile = new Profile();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
        user.setEmail(userRegisterDto.getEmail());
        user.addRole(roleRepository.findByName("ROLE_MEMBER"));
        profile.setFirstName(userRegisterDto.getFirstName());
        profile.setLastName(userRegisterDto.getLastName());
        profile = profileRepository.save(profile);
        if(!userRepository.findById(profile.getId()).isPresent()){
            user.setId(profile.getId());
        }
        user.setProfile(profile);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto addRoleById(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role  = roleRepository.findById(roleId);
        if(!user.isPresent() || !role.isPresent()){
            throw new IllegalArgumentException("User or role not found");
        }
        boolean isAdded = false;
        for(Role r : user.get().getRoles()){
            if (r.getName().equals(role.get().getName()))
                isAdded = true;
        }
        if(!isAdded){
            user.get().addRole(role.get());
            userRepository.save(user.get());
            return modelMapper.map(user.get(), UserDto.class);
        }
        throw new IllegalArgumentException("Role is already added");
    }

    @Override
    public Boolean delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            userRepository.delete(user.get());
            return Boolean.TRUE;
        }
        else{
            throw new IllegalArgumentException("user not found");
        }
    }


}
