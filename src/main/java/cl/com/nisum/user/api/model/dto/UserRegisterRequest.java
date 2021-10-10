package cl.com.nisum.user.api.model.dto;

import cl.com.nisum.user.api.util.UserUtil;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

    @NotBlank(message = "El nombre no puede ser vacío")
    private String name;

    @Email(regexp = UserUtil.EMAIL_REGEX, message = "El correo tiene formato incorrecto")
    @NotBlank(message = "El correo no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede ser vacío")
    @Pattern(regexp = UserUtil.PASSWORD_REGEX, message = "La contraseña tiene formato incorrecto")
    private String password;

    @NotEmpty(message = "Lista de teléfonos inválida")
    @Valid
    private List<PhoneRequest> phones;

}
