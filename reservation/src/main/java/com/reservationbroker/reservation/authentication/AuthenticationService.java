package com.reservationbroker.reservation.authentication;

import com.reservationbroker.reservation.dto.LoginDTO;
import com.reservationbroker.reservation.dto.LoginResponse;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.jwt.JwtService;
import com.reservationbroker.reservation.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private  final JwtService jwtService;

    private final TokenBlackListService tokenBlackListService;

    private final PasswordEncoder passwordEncoder;




    public LoginResponse register(User request){
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        return  new LoginResponse(token);
    }




    public LoginResponse login(LoginDTO authenticationRequest){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return new LoginResponse(jwt);
    }

    public void logout(String token) {
        tokenBlackListService.blacklistToken(token);
        SecurityContextHolder.clearContext();
    }


    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("ID_usera" , user.getId());
        extraClaims.put("ID_kompanije" , user.getCompany().getId());
        extraClaims.put("Username" , user.getUsername());
        extraClaims.put("Role" ,user.getRole().getName() );
        extraClaims.put("Permisije" ,user.getAuthorities() );
        return extraClaims;
    }
}
