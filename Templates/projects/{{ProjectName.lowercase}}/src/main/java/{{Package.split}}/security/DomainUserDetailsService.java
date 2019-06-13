package {{Package}}.security;

import {{Package}}.domain.User;
import {{Package}}.exception.UserNotActivatedException;
import {{Package}}.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-05-28 17:57:06
 * Description:
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> userFromDatabase = userRepository.findOneByLoginName(login);
        if(!userFromDatabase.isPresent()) {
            log.info("Not found user by login name {}, try to found with user name", login);
            userFromDatabase = userRepository.findOneByLoginName(login);
        }
        if(!userFromDatabase.isPresent()) {
            log.info("Not found user by name {}, try to found with user phone", login);
            userFromDatabase = userRepository.findOneByMobilePhone(login);
        }

        return userFromDatabase.map(user -> {
            if (user.getStatus() == 0) {
                throw new UserNotActivatedException("User " + login + " was not activated");
            }

            return new JWTUser(user.getLoginName(), user.getPassword(), user.getAuthority());
        }).orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the " +
                "database"));
    }
}
