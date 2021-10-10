package cl.com.nisum.user.api.resource;

import cl.com.nisum.user.api.exception.NotFoundException;
import cl.com.nisum.user.api.model.dto.UserRegisterRequest;
import cl.com.nisum.user.api.model.dto.UserResponse;
import cl.com.nisum.user.api.model.dto.UserUpdateRequest;
import cl.com.nisum.user.api.service.TokenService;
import cl.com.nisum.user.api.service.UserService;
import cl.com.nisum.user.api.util.FileTestUtil;
import cl.com.nisum.user.api.util.UserTestMockUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class, properties = "spring.profiles.active=local")
@Import(TokenService.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenGetUsers_thenReturnUsers() throws Exception {
        String userId = "dc77c429-17ba-47ac-bb8a-1b0cf3614334";
        LocalDateTime date = getDate();
        String jsonResponseExpected = FileTestUtil.readFile("/json/findAllUsersResponse.json");

        given(userService.findAllUsers()).willReturn(Arrays.asList(UserTestMockUtil.getUserResponseMock(userId, date)));

        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(jsonResponseExpected));
    }

    @Test
    public void givenUserIdExists_whenGetUserById_thenReturnUserResponseWithStatusOk() throws Exception {
        String userId = "dc77c429-17ba-47ac-bb8a-1b0cf3614334";
        LocalDateTime date = getDate();
        String jsonResponseExpected = FileTestUtil.readFile("/json/findUserByIdResponse.json");

        given(userService.findUserById(userId)).willReturn(UserTestMockUtil.getUserResponseMock(userId, date));

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponseExpected));
    }

    @Test
    public void givenUserIdNotExists_whenGetUserById_thenReturnStatusNotFound() throws Exception {
        String userId = "RRRRR-AAA-NNNN-DDD-OO-MM";

        doThrow(new NotFoundException("Usuario no encontrado")).when(this.userService).findUserById(userId);

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Usuario no encontrado")));
    }

    @Test
    public void givenUserRegisterRequest_whenPostUser_thenReturnUserResponseWithStatusCreated() throws Exception {
        UserRegisterRequest request = UserTestMockUtil.getUserRegisterRequest();
        String jsonResponseExpected = FileTestUtil.readFile("/json/saveUserResponse.json");
        String userId = "02d1a98b-26dc-4c92-8079-918ccbc2382b";
        LocalDateTime date = getDate();

        given(userService.saveUser(any(UserRegisterRequest.class))).willReturn(UserTestMockUtil.getUserResponseMock(userId, date));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponseExpected));
    }

    @Test
    public void givenUserRegisterRequestInvalid_whenPostUser_thenReturnUserResponseWithStatusBadRequest() throws Exception {
        UserRegisterRequest request = UserTestMockUtil.getUserRegisterRequest();

        request.setName("");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("El nombre no puede ser vacío")));;;
    }

    @Test
    public void givenUserUpdateRequest_whenPostUser_thenReturnUserResponseWithStatusOk() throws Exception {
        String newName = "Nuevo Nombre";
        String jsonResponseExpected = FileTestUtil.readFile("/json/updateUserResponse.json");
        String userId = "02d1a98b-26dc-4c92-8079-918ccbc2382b";
        LocalDateTime date = getDate();
        UserUpdateRequest request = UserTestMockUtil.getUserUpdateRequest(newName);
        UserResponse userResponseExpected = UserTestMockUtil.getUserResponseMock(userId, date);

        userResponseExpected.setName(newName);

        given(userService.updateUser(anyString(), any(UserUpdateRequest.class))).willReturn(userResponseExpected);

        mockMvc.perform(patch("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponseExpected));
    }

    @Test
    public void givenUserIdExists_whenDeleteUser_thenReturnStatusNoContent() throws Exception {
        String userId = "02d1a98b-26dc-4c92-8079-918ccbc2382b";

        mockMvc.perform(delete("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenUserIdNotExists_whenDeleteUserById_thenReturnStatusNotFound() throws Exception {
        String userId = "RRRRR-AAA-NNNN-DDD-OO-MM";

        doThrow(new NotFoundException("Usuario no encontrado")).when(this.userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("Usuario no encontrado")));
    }

    private LocalDateTime getDate() {
        return LocalDateTime.of(2021, 10, 9, 14, 14, 8, 579);
    }
}
