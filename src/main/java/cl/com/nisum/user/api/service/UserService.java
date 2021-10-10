package cl.com.nisum.user.api.service;

import cl.com.nisum.user.api.dao.OperationsDao;
import cl.com.nisum.user.api.exception.NotFoundException;
import cl.com.nisum.user.api.exception.UnprocessableEntity;
import cl.com.nisum.user.api.mapper.UserMapper;
import cl.com.nisum.user.api.model.dto.UserUpdateRequest;
import cl.com.nisum.user.api.model.dto.UserResponse;
import cl.com.nisum.user.api.model.dto.UserRegisterRequest;
import cl.com.nisum.user.api.model.entity.User;
import cl.com.nisum.user.api.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final OperationsDao<User> userOperationsDao;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userOperationsDao.findAll().stream()
                .map(UserMapper::buildGetUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(String id) {
        return UserMapper.buildGetUserResponse(returnAndValidateExistsUserById(id));
    }

    public UserResponse saveUser(UserRegisterRequest request) {
        validateExistsUserByEmail(request.getEmail());

        String idGenerated = UserUtil.generateId();
        User user = UserMapper.buildUserEntity(request, idGenerated, tokenService, passwordEncoder);

        log.info("Registering user: {}", user);

        return UserMapper.buildGetUserResponse(userOperationsDao.saveOrUpdate(user));
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User userDB = returnAndValidateExistsUserById(id);
        userDB.populateUserEntityByUpdate(request);

        log.info("Updating user. ID: {}, Request: {}, UserDB: {}", id, request, userDB);

        return UserMapper.buildUpdatedUserResponse(userOperationsDao.saveOrUpdate(userDB));
    }

    public void deleteUser(String id) {
        User userDB = returnAndValidateExistsUserById(id);

        log.info("Deleting user. ID: {}, UserDB: {}", id, userDB);
        userOperationsDao.delete(userDB);
    }

    private User returnAndValidateExistsUserById(String id) {
        return userOperationsDao.findOne("id", id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private void validateExistsUserByEmail(String email) {
        userOperationsDao.findOne("email", email).ifPresent(user -> {
            throw new UnprocessableEntity("El correo ya registrado");
        });
    }
}
