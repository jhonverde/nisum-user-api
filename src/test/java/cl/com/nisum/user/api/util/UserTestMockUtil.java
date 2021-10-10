package cl.com.nisum.user.api.util;

import cl.com.nisum.user.api.model.constant.UserStatus;
import cl.com.nisum.user.api.model.dto.PhoneRequest;
import cl.com.nisum.user.api.model.dto.UserRegisterRequest;
import cl.com.nisum.user.api.model.dto.UserResponse;
import cl.com.nisum.user.api.model.dto.UserUpdateRequest;
import cl.com.nisum.user.api.model.entity.Phone;
import cl.com.nisum.user.api.model.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UserTestMockUtil {

    private UserTestMockUtil() {
        throw new UnsupportedOperationException();
    }

    public static User getUserMock() {
        List<Phone> phoneExpectedList = new ArrayList<>();
        User userExpected = User
                .builder()
                .id(UserUtil.generateId())
                .name("Jhon Verde")
                .email("jhon.verde@test.cl")
                .password("&&&123BJKKKkkkk&&/")
                .token("ejjjjjj.bbbbb.ccccc")
                .isActive(UserStatus.INACTIVE)
                .phoneList(phoneExpectedList)
                .build();

        phoneExpectedList.add(Phone
                .builder()
                .user(userExpected)
                .number("741852963")
                .cityCode("1")
                .countryCode("12")
                .build());

        return userExpected;
    }

    public static User getUserSaveMock(LocalDateTime date) {
        User user = getUserMock();

        user.setName(null);
        user.setPassword(null);
        user.setLastLogin(date);
        user.setCreatedDate(date);
        user.setUpdatedDate(date);

        return user;
    }

    public static UserResponse getUserResponseMock(String id, LocalDateTime date) {
        UserResponse userResponse = UserResponse
                .builder()
                .id(id)
                .token("ejjjjjj.bbbbb.ccccc")
                .isactive(UserStatus.INACTIVE)
                .lastLogin(date)
                .created(date)
                .modified(date)
                .build();

        return userResponse;
    }

    public static UserRegisterRequest getUserRegisterRequest() {
        PhoneRequest phoneRequest = PhoneRequest
                .builder()
                .number("741852963")
                .citycode("1")
                .contrycode("12")
                .build();

        UserRegisterRequest request = UserRegisterRequest
                .builder()
                .name("Jhon Verde")
                .email("jhon.verde@test.cl")
                .password("Test2021")
                .phones(Arrays.asList(phoneRequest))
                .build();

        return request;
    }

    public static UserUpdateRequest getUserUpdateRequest(String newName) {
        return UserUpdateRequest
                .builder()
                .name(newName)
                .build();
    }
}
