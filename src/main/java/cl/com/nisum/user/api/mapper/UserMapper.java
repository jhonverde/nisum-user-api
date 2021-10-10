package cl.com.nisum.user.api.mapper;

import cl.com.nisum.user.api.model.constant.UserStatus;
import cl.com.nisum.user.api.model.dto.*;
import cl.com.nisum.user.api.model.entity.Phone;
import cl.com.nisum.user.api.model.entity.User;
import cl.com.nisum.user.api.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException();
    }

    public static User buildUserEntity(UserRegisterRequest request, String idGenerated, TokenService tokenService, PasswordEncoder passwordEncoder) {
        List<Phone> phoneList = new ArrayList<>();
        User user = User
                .builder()
                .id(idGenerated)
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(UserStatus.INACTIVE)
                .token(tokenService.generateTokenByUserType(request.getEmail()))
                .phoneList(phoneList)
                .build();

        request.getPhones().stream().forEach(phoneRequest -> phoneList.add(buildPhoneEntity(user, phoneRequest)));

        return user;
    }

    public static Phone buildPhoneEntity(User user, PhoneRequest phoneRequest) {
        return Phone
                .builder()
                .user(user)
                .number(phoneRequest.getNumber())
                .cityCode(phoneRequest.getCitycode())
                .countryCode(phoneRequest.getContrycode())
                .build();
    }

    public static UserResponse buildGetUserResponse(User entity) {
        return UserResponse
                .builder()
                .id(entity.getId())
                .created(entity.getCreatedDate())
                .modified(entity.getUpdatedDate())
                .lastLogin(entity.getLastLogin())
                .token(entity.getToken())
                .isactive(entity.getIsActive())
                .build();
    }

    public static UserResponse buildUpdatedUserResponse(User entity) {
        return UserResponse
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .created(entity.getCreatedDate())
                .modified(entity.getUpdatedDate())
                .lastLogin(entity.getLastLogin())
                .token(entity.getToken())
                .isactive(entity.getIsActive())
                .build();
    }
}
