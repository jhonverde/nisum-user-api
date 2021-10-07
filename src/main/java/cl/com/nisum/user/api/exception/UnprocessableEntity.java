package cl.com.nisum.user.api.exception;

public class UnprocessableEntity extends RuntimeException {

  public UnprocessableEntity(String message) {
    super(message);
  }

}
