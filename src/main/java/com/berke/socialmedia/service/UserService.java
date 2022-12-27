package com.berke.socialmedia.service;

import com.berke.socialmedia.repository.UserRepository;
import com.berke.socialmedia.entity.Privilege;
import com.berke.socialmedia.entity.Profile;
import com.berke.socialmedia.entity.Role;
import com.berke.socialmedia.entity.User;
import com.berke.socialmedia.dto.user.UserDto;
import com.berke.socialmedia.dto.user.UserRegisterDto;
import com.berke.socialmedia.dto.user.UserRequestDto;
import com.berke.socialmedia.exception.AlreadyExistsException;
import com.berke.socialmedia.exception.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(value = "userService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    private  ModelMapper modelMapper;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;




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


    public List<UserDto> getAll() {
        return Arrays.asList(modelMapper.map(userRepository.findAll(),UserDto[].class));
    }


    public UserDto getById(Long id) {
        User user = checkAndGetUserById(id);

        return modelMapper.map(user,UserDto.class);
    }


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


    public UserDto register(UserRegisterDto userRegisterDto) {
        User user = new User();
        Profile profile = new Profile();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDto.getPassword()));
        user.setEmail(userRegisterDto.getEmail());
        user.addRole(roleService.findByNameFromRepository("ROLE_MEMBER"));
        profile.setFirstName(userRegisterDto.getFirstName());
        profile.setLastName(userRegisterDto.getLastName());
        profile.setUser(user);
        user.setProfile(profile);
        //profileRepository.save(profile);
        user = userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }


    public UserDto update(Long id, UserRequestDto userRequestDto) {
        User user = checkAndGetUserById(id);

        if(userRequestDto.getEmail() != null){
            user.setEmail(userRequestDto.getEmail());
        }
        if(userRequestDto.getUsername() != null){
            user.setUsername(userRequestDto.getUsername());
        }
        return modelMapper.map(userRepository.save(user), UserDto.class);

    }

    public Boolean delete(Long id) {
        User user = checkAndGetUserById(id);
        userRepository.delete(user);
        return Boolean.TRUE;
    }


    public UserDto addRoleById(Long userId, Long roleId) {
        User user = checkAndGetUserById(userId);
        Role role = roleService.checkAndGetRoleById(roleId);

        user.addRole(role);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }


    public Boolean removeRoleById(Long userId, Long roleId) {
        User user = checkAndGetUserById(userId);
        Role role = roleService.checkAndGetRoleById(roleId);
        user.removeRole(role);
        userRepository.save(user);
        return Boolean.TRUE;
    }

    protected User checkAndGetUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new NotFoundException("User", "No user found with this id");
        return user.get();
    }


    protected void checkAndThrowErrorIfUserExists(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            throw new AlreadyExistsException("User", "A user with this id already exists");
    }

    protected User checkAndGetUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User", "No user found with this username");
        return user;
    }

    protected User getCurrentUser(){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return checkAndGetUserByUsername(currentUserName);
        }else{
            throw new RuntimeException("No User");
        }

    }
}
