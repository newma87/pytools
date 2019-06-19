package {{Package}}.repository;

import {{Package}}.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: AutoCoder (maintained by newma<newma@live.cn>)
 * Create: 2019-06-19 21:15:14
 */

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByMobilePhone(String mobilePhone);
    Optional<User> findOneByLoginName(String loginName);
}