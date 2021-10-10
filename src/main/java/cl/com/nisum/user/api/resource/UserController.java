package cl.com.nisum.user.api.resource;

import cl.com.nisum.user.api.model.dto.UserUpdateRequest;
import cl.com.nisum.user.api.model.dto.UserResponse;
import cl.com.nisum.user.api.model.dto.UserRegisterRequest;
import cl.com.nisum.user.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse findUserById(@PathVariable("id") String id) {
        return userService.findUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse saveUser(@Valid @RequestBody UserRegisterRequest request) {
        return userService.saveUser(request);
    }

    @PatchMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") String id, @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") String id) {
        userService.deleteUser(id);
    }
}
