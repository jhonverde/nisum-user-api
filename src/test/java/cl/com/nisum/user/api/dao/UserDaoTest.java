package cl.com.nisum.user.api.dao;

import cl.com.nisum.user.api.NisumUserApiApplication;
import cl.com.nisum.user.api.model.entity.User;
import cl.com.nisum.user.api.util.UserTestMockUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NisumUserApiApplication.class)
@Transactional
public class UserDaoTest {

    @Autowired
    private OperationsDao<User> userOperationsDao;

    private User userExpected;

    private User userResponse;

    @Before
    public void setup() {
        userExpected = UserTestMockUtil.getUserMock();
        userResponse = userOperationsDao.saveOrUpdate(userExpected);
    }

    @Test
    public void whenFindAllUsers_thenReturnUsers() {
        List<User> userListExpected = Arrays.asList(userExpected);
        List<User> userListResponse = userOperationsDao.findAll();

        assertThat(userListResponse).usingFieldByFieldElementComparator().isEqualTo(userListExpected);
    }

    @Test
    public void givenUserEmailExists_whenFindUserByEmail_thenReturnUser() {
        Optional<User> opUserReponse = userOperationsDao.findOne("email","jhon.verde@test.cl");

        assertThat(opUserReponse.isPresent()).isEqualTo(true);
        assertThat(opUserReponse.get()).usingRecursiveComparison()
                .ignoringFields("lastLogin", "createdDate", "updatedDate").isEqualTo(userExpected);
    }

    @Test
    public void givenUserIdExists_whenFindUserById_thenReturnUser() {
        Optional<User> opUserResponse = userOperationsDao.findOne("id", userExpected.getId());

        assertThat(opUserResponse.isPresent()).isEqualTo(true);
        assertThat(opUserResponse.get()).usingRecursiveComparison()
                .ignoringFields("lastLogin", "createdDate", "updatedDate").isEqualTo(userExpected);
    }

    @Test
    public void givenUserEntity_whenSaveUser_thenReturnUser() {
        assertThat(userResponse).usingRecursiveComparison()
                .ignoringFields("lastLogin", "createdDate", "updatedDate").isEqualTo(userExpected);
    }

    @Test
    public void givenUserEntity_whenDeleteUser_thenOk() {
        userOperationsDao.delete(userResponse);

        Optional<User> opUserDB = userOperationsDao.findOne("id", userExpected.getId());

        assertThat(opUserDB.isPresent()).isEqualTo(false);
    }

}
