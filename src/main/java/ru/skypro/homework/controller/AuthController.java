package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Авторизация пользователя",
            responses = {@ApiResponse(responseCode = "200",
                            description = "OK",
                    content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Регистрация пользователя",
            responses = {@ApiResponse(
                            responseCode = "201",
                            description = "Created",
                    content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request",
                            content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
