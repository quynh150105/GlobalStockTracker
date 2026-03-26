package com.quynhproject.globalstocktracker.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import com.quynhproject.globalstocktracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

   private final UserRepository userRepository;

   private final PasswordEncoder passwordEncoder;

   @Value("${jwt.signerKey}")
   private String signerKey;


    @Override
    public String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 86400000))
                .jwtID(UUID.randomUUID().toString())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,  payload);

        try{
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("cannot create token");
            throw new AppException("khong the tao token");
        }
    }

    @Override
    public LoginUserResponse login(LoginUserRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow( () -> new AppException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw  new AppException("Wrong password");
        }

        return LoginUserResponse.builder()
                .token(generateToken(request.getUsername()))
                .authenticated(true)
                .build();
    }
}
