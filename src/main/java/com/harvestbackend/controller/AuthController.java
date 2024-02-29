package com.harvestbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harvestbackend.exceptions.TokenRefreshException;
import com.harvestbackend.model.*;
import com.harvestbackend.payload.request.FarmerSignUpRequest;
import com.harvestbackend.payload.request.LoginRequest;
import com.harvestbackend.payload.request.SignUpRequest;
import com.harvestbackend.payload.request.TokenRefreshRequest;
import com.harvestbackend.payload.response.JwtResponse;
import com.harvestbackend.payload.response.MessageResponse;
import com.harvestbackend.payload.response.TokenRefreshResponse;
import com.harvestbackend.repository.RoleRepository;
import com.harvestbackend.repository.UserRepository;
import com.harvestbackend.security.jwt.JwtUtils;
import com.harvestbackend.security.services.RefreshTokenService;
import com.harvestbackend.security.services.UserDetailsImpl;
import com.harvestbackend.services.CloudinaryImageService;
import com.harvestbackend.services.FarmerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(value = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    FarmerService farmerService;
    @Autowired
    private CloudinaryImageService cloudinaryImageService;
    @Autowired
    ObjectMapper objectMapper;
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webappimages";


    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((UserDetailsImpl) authentication.getPrincipal());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        refreshTokenService.deleteByUserId(userDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), roles));

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("signout/{id}")
    public ResponseEntity<?> signOut(@PathVariable Long id ) {
        return ResponseEntity.ok().body(refreshTokenService.deleteByUserId(id));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest ) {
//       SignUpRequest signUpRequest =null;
//        try {
//                signUpRequest = objectMapper.readValue( signUpRequest1 ,SignUpRequest.class);
//
//        }catch (JsonProcessingException e){
//            return ResponseEntity.badRequest().body(new MessageResponse("invalid user"));
//
//        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use"));
        }

        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found"));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_FARMER)
                                .orElseThrow(() -> new RuntimeException("Role is not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
//        user.setAddress(signUpRequest.getAddress());

       User user1 = userRepository.save(user);

//        String imgUrl;
//
//        try {
//           Map upload = this.cloudinaryImageService.upload(profile);
//           imgUrl = (String) upload.get("url");
//        }
//       catch (IOException e){
//           throw new RuntimeException("error in uploading !!");
//       }
//        user1.setProfileImage(imgUrl);
//        userRepository.save(user1);


        return ResponseEntity.ok(new MessageResponse("User is registered successfully" ));
    }


    @PostMapping("/adduserprofile")
    public ResponseEntity<String> addProfilePic(@RequestParam("img") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file name");
        }


            UUID uuid = UUID.randomUUID();
            originalFileName = uuid.toString() + "_" + originalFileName;

            Path fileNameAndPath = Paths.get(uploadDirectory, originalFileName);
            Files.write(fileNameAndPath, file.getBytes());

            User user = userRepository.findByUsername("john_doe").orElseThrow(() -> new RuntimeException("User not found"));
            user.setProfileImage(originalFileName);
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body("Image added successfully");



    }


}
