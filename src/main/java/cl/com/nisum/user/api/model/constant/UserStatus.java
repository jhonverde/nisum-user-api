package cl.com.nisum.user.api.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum UserStatus {

  ACTIVE(1), INACTIVE(0);

  private Integer code;

  public static UserStatus getByCode(Integer code) {
    return Arrays.stream(values())
        .filter(value -> code != null && value.getCode().compareTo(code) == 0)
        .findAny().orElseThrow(IllegalArgumentException::new);
  }

}
