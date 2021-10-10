package cl.com.nisum.user.api.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserUtilTest {

    @Test
    public void givenEmailValid_whenValidateRegex_thenTrue() {
        String email = "a@aa.cl";

        assertTrue(email.matches(UserUtil.EMAIL_REGEX));
    }

    @Test
    public void givenEmailInValid_whenValidateRegex_thenFalse() {
        String email = "a@aa..cl";

        assertFalse(email.matches(UserUtil.EMAIL_REGEX));
    }

    @Test
    public void givenPasswordValid_whenValidateRegex_thenTrue() {
        String email = "Az12";

        assertTrue(email.matches(UserUtil.PASSWORD_REGEX));
    }

    @Test
    public void givenPasswordInValid_whenValidateRegex_thenFalse() {
        String email = "aa1";

        assertFalse(email.matches(UserUtil.PASSWORD_REGEX));
    }

}
