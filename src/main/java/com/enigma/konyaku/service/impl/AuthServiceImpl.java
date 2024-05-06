package com.enigma.konyaku.service.impl;

import com.enigma.konyaku.constant.UserRole;
import com.enigma.konyaku.dto.request.AuthRequest;
import com.enigma.konyaku.dto.request.RegisterRequest;
import com.enigma.konyaku.dto.response.LoginResponse;
import com.enigma.konyaku.dto.response.RegisterResponse;
import com.enigma.konyaku.entity.*;
import com.enigma.konyaku.repository.UserAccountRepository;
import com.enigma.konyaku.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final AddressService addressService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AccountImageService accountImageService;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final AuthenticationManager authenticationManager;

    @Value("${konyaku.username.superadmin}")
    private String superAdminUsername;
    @Value("${konyaku.password.superadmin}")
    private String superAdminPassword;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        Optional<UserAccount> currentUser = userAccountRepository.findByUsername(superAdminUsername);
        if (currentUser.isPresent()) return;

        Role superAdmin = roleService.getOrSave(UserRole.ROLE_SUPER_ADMIN);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role user = roleService.getOrSave(UserRole.ROLE_USER);
        Role vendor = roleService.getOrSave(UserRole.ROLE_VENDOR);

        UserAccount account = UserAccount.builder()
                .username(superAdminUsername)
                .password(passwordEncoder.encode(superAdminPassword))
                .role(List.of(superAdmin, admin, user, vendor))
                .isEnable(true)
                .build();

        userAccountRepository.save(account);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(RegisterRequest request) {
        Role userRole = roleService.getOrSave(UserRole.ROLE_USER);
        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Role vendor = roleService.getOrSave(UserRole.ROLE_VENDOR);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .username(request.getUsername())
                        .password(hashPassword)
                        .role(List.of(userRole, admin, vendor))
                        .isEnable(true)
                        .build()
        );

        Address address = addressService.create(
                request.getAddressRequest()
        );

        User user = userService.create(
                User.builder()
                        .name(request.getName())
                        .mobilePhoneNo(request.getMobilePhoneNo())
                        .address(address)
                        .userAccount(account)
                        .build()
        );

        List<AccountImage> images = accountImageService.create(
                request.getImages().stream().map(
                        (image) -> {
                            Image createdImage = imageService.create(image);
                            return AccountImage.builder()
                                    .image(createdImage)
                                    .user(user)
                                    .build();
                        }
                ).toList());

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse updateRoles(String id) {
        Optional<UserAccount> account = userAccountRepository.findById(id);
        if (account.isEmpty()) throw new RuntimeException("Account not found");

        List<Role> roles = account.get().getRole();
        roles.add(roleService.getOrSave(UserRole.ROLE_VENDOR));

        UserAccount updatedAccount = account.get();
        updatedAccount.setRole(roles);
        updatedAccount = userAccountRepository.saveAndFlush(updatedAccount);

        List<String> updatedAccountRoles = updatedAccount.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return RegisterResponse.builder()
                .username(updatedAccount.getUsername())
                .roles(updatedAccountRoles)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest request) throws DataIntegrityViolationException {
        Role role = roleService.getOrSave(UserRole.ROLE_USER);
        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = userAccountRepository.saveAndFlush(
                UserAccount.builder()
                        .username(request.getUsername())
                        .password(hashPassword)
                        .role(List.of(role))
                        .isEnable(true)
                        .build()
        );

        Address address = addressService.create(
                request.getAddressRequest()
        );

        User user = userService.create(
                User.builder()
                        .name(request.getName())
                        .mobilePhoneNo(request.getMobilePhoneNo())
                        .address(address)
                        .userAccount(account)
                        .build()
        );

        List<AccountImage> images = accountImageService.create(
                request.getImages().stream().map(
                    (image) -> {
                        Image createdImage = imageService.create(image);
                        return AccountImage.builder()
                                .image(createdImage)
                                .user(user)
                                .build();
                    }
        ).toList());

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        System.out.println(roles.get(0));

        return RegisterResponse.builder()
                .username(account.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .userAccountId(userAccount.getId())
                .username(userAccount.getUsername())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }

    @Override
    public boolean validateToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getPrincipal().toString())
                .orElse(null);
        return userAccount != null;
    }
}
