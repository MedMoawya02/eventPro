package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.UserRegistrationRequest;
import com.eventpro.eventproapi.dto.response.UserResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class userService {
    private final UserRepository userRepository;
    @Transactional
    public UserResponse createUtilisateur(UserRegistrationRequest request){
        User user;
        if("CLIENT".equals(request.getRole())){
            Client client=new Client();
            client.setName(request.getName());
            client.setEmail(request.getEmail());
            client.setPhone(request.getPhone());
            user=client;
        } else if ("ORGANIZER".equals(request.getRole())) {
            Organizer organizer=new Organizer();
            organizer.setName(request.getName());
            organizer.setEmail(request.getEmail());
            organizer.setOrganisationName(request.getOrganisationName());
            user=organizer;
        }else {
            throw new IllegalArgumentException("Role non supporté "+request.getRole());
        }
        User saved=userRepository.save(user);
        return mapToResponse(saved) ;
    }

    private UserResponse mapToResponse(User user){
        UserResponse.UserResponseBuilder builder=UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail());
        if(user instanceof Client){
            builder.role("CLIENT").phone(((Client)user).getPhone());
        } else if (user instanceof Organizer) {
            builder.role("ORGANIZER").organisationName(((Organizer) user).getOrganisationName());
        }else if (user instanceof Administrateur) {
            builder.role("ADMIN");
        } else if (user instanceof ControlAgent) {
            builder.role("CONTROLAGENT");
        }
        return builder.build();
    }
}
