package com.eventpro.eventproapi.service;

import com.eventpro.eventproapi.dto.request.UserRegistrationRequest;
import com.eventpro.eventproapi.dto.response.UserResponse;
import com.eventpro.eventproapi.entity.*;
import com.eventpro.eventproapi.exception.ResourceNotFoundException;
import com.eventpro.eventproapi.repository.ReservationRepository;
import com.eventpro.eventproapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
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
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public void delete(Long id){
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("utilisateur not found "+id));
        if(user instanceof Client){
            Client client=(Client)user;
            if(reservationRepository.existsByClientId(client.getId())){
                throw new IllegalArgumentException("Impossible de supprimé un client ayant des reservations");
            }
        }
        userRepository.delete(user);
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
