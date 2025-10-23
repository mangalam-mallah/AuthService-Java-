package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.entities.UserInfo;
import org.example.eventProducer.UserInfoEvent;
import org.example.eventProducer.UserInfoProducer;
import org.example.model.UserInfoDTO;
import org.example.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Data
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired private final UserRepo userRepo;
    @Autowired private final PasswordEncoder passwordEncoder;
    @Autowired private final UserInfoProducer userInfoProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Could not found user");
        }
        return new CustomUserDetail(user);
    }

    public UserInfo ifUserAlreadyExists(UserInfoDTO userInfoDTO){
        return userRepo.findByUsername(userInfoDTO.getUsername());
    }

    public String signupUser(UserInfoDTO userInfoDTO){
        userInfoDTO.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
        if(Objects.nonNull(ifUserAlreadyExists(userInfoDTO))){
            return null ;
        }
        String userId = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo(userId, userInfoDTO.getUsername(), userInfoDTO.getPassword(), new HashSet<>());
        userRepo.save(userInfo);
        userInfoProducer.sendEventKafka(userInfoEventToPublish(userInfoDTO, userId));
        return userId;
    }

    public String getUserByUsername(String username){
        return Optional.of(userRepo.findByUsername(username)).map(UserInfo::getUserId).orElse(null);
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDTO userInfoDTO, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDTO.getFirstName())
                .lastName(userInfoDTO.getLastName())
                .email(userInfoDTO.getEmail())
                .phoneNumber(userInfoDTO.getPhoneNumber())
                .build();
    }
}
