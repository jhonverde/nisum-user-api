package cl.com.nisum.user.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class PhoneRequest {

    @NotBlank(message = "El número es inválido")
    private String number;

    @NotBlank(message = "El código de ciudad es inválido")
    private String citycode;

    @NotBlank(message = "El código de país es inválido")
    private String contrycode;

}
