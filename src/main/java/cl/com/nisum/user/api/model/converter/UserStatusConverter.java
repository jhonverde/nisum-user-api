package cl.com.nisum.user.api.model.converter;

import cl.com.nisum.user.api.model.constant.UserStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(UserStatus attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public UserStatus convertToEntityAttribute(Integer dbData) {
    return UserStatus.getByCode(dbData);
  }

}
