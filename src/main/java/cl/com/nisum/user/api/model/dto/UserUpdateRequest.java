package cl.com.nisum.user.api.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserUpdateRequest {

    @NotBlank(message = "El nombre no puede ser vacío")
    private String name;

}
