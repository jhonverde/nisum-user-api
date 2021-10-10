package cl.com.nisum.user.api.service;

import cl.com.nisum.user.api.dao.OperationsDao;
import cl.com.nisum.user.api.dao.UserDao;
import cl.com.nisum.user.api.exception.NotFoundException;
import cl.com.nisum.user.api.exception.UnprocessableEntity;
import cl.com.nisum.user.api.model.dto.UserRegisterRequest;
import cl.com.nisum.user.api.model.dto.UserResponse;
import cl.com.nisum.user.api.model.dto.UserUpdateRequest;
import cl.com.nisum.user.api.model.entity.User;
import cl.com.nisum.user.api.util.UserTestMockUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @MockBean
    private OperationsDao<User> userDao;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private User userMock;

    private UserResponse userResponseExpected;

    @Before
    public void setup() {
        userService = new UserService(userDao, tokenService, passwordEncoder);
        LocalDateTime date = LocalDateTime.now();
        userMock = UserTestMockUtil.getUserSaveMock(date);
        userResponseExpected = UserTestMockUtil.getUserResponseMock(userMock.getId(), date);
    }

    @Test
    public void whenFindAllUsers_thenReturnUsers() {
        List<UserResponse> userListExpected = Arrays.asList(userResponseExpected);
        List<UserResponse> userListResponse;

        given(userDao.findAll()).willReturn(Arrays.asList(userMock));

        userListResponse = userService.findAllUsers();

        assertThat(userListResponse).usingFieldByFieldElementComparator().isEqualTo(userListExpected);
    }

    @Test
    public void givenUserIdExists_whenFindUserById_thenReturnUser() {
        String userId = userResponseExpected.getId();
        given(userDao.findOne(eq("id"), eq(userId))).willReturn(Optional.ofNullable(userMock));

        UserResponse userResponse = userService.findUserById(userId);

        assertThat(userResponse).usingRecursiveComparison().isEqualTo(userResponseExpected);
    }

    @Test
    public void givenUserIdNotExists_whenFindUserById_thenReturnNotFoundException() {
        String userId = userResponseExpected.getId();
        given(userDao.findOne(eq("id"), eq(userId))).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(userId))
                .isInstanceOf(NotFoundException.class).hasMessage("Usuario no encontrado");
    }

    @Test
    public void givenUserEntity_whenSaveUser_thenReturnUser() {
        UserRegisterRequest request = UserTestMockUtil.getUserRegisterRequest();

        given(userDao.findOne(eq("email"), eq(request.getEmail()))).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("&&&123BJKKKkkkk&&/");
        given(tokenService.generateTokenByUserType(anyString())).willReturn("ejjjjjj.bbbbb.ccccc");
        given(userDao.saveOrUpdate(any(User.class))).willReturn(userMock);

        UserResponse userResponse = userService.saveUser(request);
        assertThat(userResponse).usingRecursiveComparison().ignoringFields("id").isEqualTo(userResponseExpected);
    }

    @Test
    public void givenUserEntityWithExistsEmail_whenSaveUser_thenReturnUnprocessableEntityException() {
        UserRegisterRequest request = UserTestMockUtil.getUserRegisterRequest();
        given(userDao.findOne("email", request.getEmail())).willReturn(Optional.ofNullable(userMock));

        assertThatThrownBy(() -> userService.saveUser(request))
                .isInstanceOf(UnprocessableEntity.class).hasMessage("El correo ya registrado");
    }

    @Test
    public void givenUserIdWithUserEntity_whenUpdateUser_thenReturnUser() {
        String userId = userResponseExpected.getId();
        String newName = "Nuevo Nombre";
        UserUpdateRequest request = UserTestMockUtil.getUserUpdateRequest(newName);

        userMock.setName(newName);

        userResponseExpected.setName(newName);

        given(userDao.findOne(eq("id"), eq(userId))).willReturn(Optional.ofNullable(userMock));
        given(userDao.saveOrUpdate(any(User.class))).willReturn(userMock);

        UserResponse userResponse = userService.updateUser(userId, request);

        assertThat(userResponse).usingRecursiveComparison().isEqualTo(userResponseExpected);
    }

    @Test
    public void givenUserId_whenDeleteUser_thenOk() {
        String userId = userResponseExpected.getId();
        given(userDao.findOne(eq("id"), eq(userId))).willReturn(Optional.ofNullable(userMock));

        userService.deleteUser(userId);

        verify(userDao, times(1)).delete(any(User.class));
    }
}
