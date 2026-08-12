package com.schoolsaathi.school_managment.security;

import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException {

        log.info("Loading user by email: {}", email);
        User user = userRepository.findByEmailAndIsDeletedFalse(email).orElseThrow(() -> {
                    log.warn("User not found: {}", email);
                    return new UsernameNotFoundException("Invalid email or password");
                });
        if (!user.getIsActive()) {
            log.warn("Inactive user tried login: {}",email);
            throw new UsernameNotFoundException("Your account has been deactivated. "+ "Contact school admin.");
        }

        // user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        //userRepository.save(user);
       return new CustomUserDetails(user);


    }
}