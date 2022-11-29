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
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import com.berke.socialmedia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

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
        User user = checkAndGetUserById(id);

        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getByUsername(String username) {
        User user = checkAndGetUserByUsername(username);

        return modelMapper.map(user, UserDto.class);
    }

    /*
    @Override
    public UserDto save(UserDto userDto) {
        checkAndThrowErrorIfUserExists(userDto.getId());

        User user = modelMapper.map(userDto,User.class);
        user = userRepository.save(user);
        userDto.setId(user.getId());
        return userDto;
    }
     */

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        checkAndThrowErrorIfUserExists(userRegisterDto.getId());
        User user = new User();
        Profile profile = new Profile();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
        user.setEmail(userRegisterDto.getEmail());
        user.addRole(roleRepository.findByName("ROLE_MEMBER"));
        profile.setFirstName(userRegisterDto.getFirstName());
        profile.setLastName(userRegisterDto.getLastName());
        profile.setUser(user);
        user.setProfile(profile);
        //profileRepository.save(profile);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }


    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = checkAndGetUserById(id);

        if(userDto.getEmail() != null){
            user.setEmail(userDto.getEmail());
        }
        if(userDto.getUsername() != null){
            user.setUsername(userDto.getUsername());
        }
        return modelMapper.map(userRepository.save(user), UserDto.class);

    }

    @Override
    public Boolean delete(Long id) {
        User user = checkAndGetUserById(id);
        userRepository.delete(user);
        return Boolean.TRUE;
    }

    @Override
    public UserDto addRoleById(Long userId, Long roleId) {
        User user = checkAndGetUserById(userId);
        Role role = checkAndGetRoleById(roleId);

        user.addRole(role);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Boolean removeRoleById(Long userId, Long roleId) {
        User user = checkAndGetUserById(userId);
        Role role = checkAndGetRoleById(roleId);
        user.removeRole(role);
        userRepository.save(user);
        return Boolean.TRUE;
    }

    private User checkAndGetUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new NotFoundException("User", "No user found with this id");
        return user.get();
    }

    private User checkAndGetUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User", "No user found with this username");
        return user;
    }
    private void checkAndThrowErrorIfUserExists(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            throw new AlreadyExistsException("User", "A user with this id already exists");
    }

    private Role checkAndGetRoleById(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(!role.isPresent())
            throw new NotFoundException("Role", "No role found with this id");
        return role.get();
    }
}
