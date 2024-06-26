package com.karty.kartyjavatest.service;

import com.karty.kartyjavatest.dto.UserDto;
import com.karty.kartyjavatest.exceptions.AlreadyExistsException;
import com.karty.kartyjavatest.exceptions.NotFoundException;
import com.karty.kartyjavatest.jwt.JwtTokenUtil;
import com.karty.kartyjavatest.model.User;
import com.karty.kartyjavatest.model.UserInfoDetails;
import com.karty.kartyjavatest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder encoder,
                            AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public User create(UserDto dto) {
        if (usersRepository.existsByUsername(dto.getUsername())) {
            throw new AlreadyExistsException("User with username [" + dto.getUsername() + "] already exists");
        }

        User user = new User(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }

    @Override
    public ResponseEntity<Object> login(UserDto dto) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        UserInfoDetails userDetails = (UserInfoDetails) authenticate.getPrincipal();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateToken(dto.getUsername()))
                .body(userDetails);
    }

    @Override
    public Iterable<User> retrieveAll() {
        return usersRepository.findAll();
    }

    @Override
    public User retrieveById(Long id) {
        User user = this.usersRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException(notFoundMessageStr(id));
        }

        return user;
    }

    @Override
    public void delete(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new NotFoundException(notFoundMessageStr(id));
        }

        usersRepository.deleteById(id);
    }

    private String notFoundMessageStr(Long id) {
        return "User with id: [" + id + "] not found";
    }
}
