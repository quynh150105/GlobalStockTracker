package com.quynhproject.globalstocktracker.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quynhproject.globalstocktracker.domain.dto.request.LoginUserRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.LogoutRequest;
import com.quynhproject.globalstocktracker.domain.dto.request.RefreshTokenRequest;
import com.quynhproject.globalstocktracker.domain.dto.response.LoginUserResponse;
import com.quynhproject.globalstocktracker.domain.dto.response.RefreshTokenResponse;
import com.quynhproject.globalstocktracker.domain.entity.InvalidatedToken;
import com.quynhproject.globalstocktracker.domain.entity.User;
import com.quynhproject.globalstocktracker.excetion.AppException;
import com.quynhproject.globalstocktracker.repository.InvalidatedTokenRepository;
import com.quynhproject.globalstocktracker.repository.UserRepository;
import com.quynhproject.globalstocktracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signerKey}")
    private String signerKey;


    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 900000))
                .jwtID(UUID.randomUUID().toString())
                .claim("type","access")
                .claim("role", user.getRole())
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
                .token(generateToken(user))
                .refreshToken(generateRefreshToken(user.getUsername()))
                .authenticated(true)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        var signJwt = verifyToken(request.getToken(), true);

        var jti = signJwt.getJWTClaimsSet().getJWTID();

        var exp = signJwt.getJWTClaimsSet().getExpirationTime();


        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(exp)
                .build();


        invalidatedTokenRepository.save(invalidatedToken);

        var username = signJwt.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException("unauthenticated"));

        String token = generateToken(user);

        return RefreshTokenResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken(), false);

        String jti = signToken.getJWTClaimsSet().getJWTID();

        Date exp = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jti)
                .expiryTime(exp)
                .build();
        log.info("logout thanh cong");
        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public String generateRefreshToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
                .jwtID(UUID.randomUUID().toString())
                .claim("type","refresh")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,  payload);

        try{
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("cannot create refresh token");
            throw new AppException("khong the tao refresh token");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expirationTime.after(new Date())))
            throw new RuntimeException("Unauthenticated");
        String tokenType = signedJWT.getJWTClaimsSet().getStringClaim("type");
        if (isRefresh && !"refresh".equals(tokenType)) {
            throw new RuntimeException("Refresh token required");
        }
        if (!isRefresh && !"access".equals(tokenType)) {
            throw new RuntimeException("Access token required");
        }
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new RuntimeException("unauthenticated");
        }

        return signedJWT;
    }


}
