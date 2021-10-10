package cl.com.nisum.user.api.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PhoneResponse {

    private Long id;

    private String number;

    private String citycode;

    private String contrycode;

}
