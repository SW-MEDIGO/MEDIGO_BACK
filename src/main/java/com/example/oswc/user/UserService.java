package com.example.oswc.user;

import com.example.oswc.auth.dto.SignupRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    public UserService(UserRepository userRepository, PasswordEncoder encoder){
        this.userRepository = userRepository; this.encoder = encoder;
    }
    public UserDocument signup(SignupRequest req){
        if(userRepository.findByUsername(req.username).isPresent()) throw new IllegalArgumentException("USERNAME_EXISTS");
        if(userRepository.existsByEmail(req.email)) throw new IllegalArgumentException("EMAIL_EXISTS");
        UserDocument u = new UserDocument();
        u.setUsername(req.username);
        u.setPasswordHash(encoder.encode(req.password));
        u.setName(req.name); u.setEmail(req.email); u.setRole("USER");
        var a = new UserDocument.Agreements();
        a.termsOfService=req.termsOfService; a.privacyPolicy=req.privacyPolicy;
        a.locationService=req.locationService; a.ageLimit=req.ageLimit; a.marketingConsent=req.marketingConsent;
        a.agreedAtEpochMillis = Instant.now().toEpochMilli();
        u.setAgreements(a);
        return userRepository.save(u);
    }
    public UserDocument authenticate(String username, String rawPassword){
        var u = userRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("INVALID_CREDENTIALS"));
        if(!encoder.matches(rawPassword, u.getPasswordHash())) throw new IllegalArgumentException("INVALID_CREDENTIALS");
        return u;
    }
}
