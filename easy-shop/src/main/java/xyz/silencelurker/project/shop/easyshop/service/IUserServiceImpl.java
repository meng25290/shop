package xyz.silencelurker.project.shop.easyshop.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import xyz.silencelurker.project.shop.easyshop.entity.User;
import xyz.silencelurker.project.shop.easyshop.repository.UserRepository;

/**
 * @author Silence_Lurker
 */
@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private MailSenderService mailSenderService;

    @Override
    public String register(String name, String password, String email) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);

        userRepository.save(user);

        user = userRepository.findAll(Example.of(user, ExampleMatcher.matching().withIgnoreNullValues())).get(0);

        template.opsForValue().set(user.getAccountId() + "", mailSenderService.confirmEmailSend(email));

        return user.getAccountId() + "";
    }

    @Override
    public User loginByName(String name, String password) {
        var user = new User();
        user.setName(name);
        user.setPassword(password);
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnoreCase("id","email").withIgnoreNullValues());

        var info = userRepository.findAll(example);

        return info.get(0);

    }

    @Override
    public User loginById(String id, String password) {
        var user = new User();
        user.setAccountId(Integer.parseInt(id));
        user.setPassword(password);
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnoreCase("name","email").withIgnoreNullValues());

        var info = userRepository.findAll(example);

        return info.get(0);
    }

    @Override
    public User loginByEmail(String email, String password) {
        var user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnoreCase("id","name").withIgnoreNullValues());

        var info = userRepository.findAll(example);

        return info.get(0);
    }

    @Override
    public User updateInfo(User newInfo) {
        var oldInfo = userRepository.findById(newInfo.getAccountId()).get();

        userRepository.save(newInfo);

        return oldInfo;
    }

    @Override
    public String emailUpdate(int accountId) {
        var user = getUserInfo(accountId);

        var checkCode = mailSenderService.confirmEmailSend(user.getEmail());

        template.opsForValue().set(checkCode, user.getAccountId() + "", Duration.ofSeconds(300));

        return checkCode;
        // WTF???
    }

    @Override
    public boolean emailUpdateConfirm(String newEmail, String checkCode, int accountId) {
        var targetId = Integer.parseInt(template.opsForValue().get(checkCode));

        if (targetId == accountId) {
            var user = getUserInfo(accountId);
            user.setEmail(newEmail);
            userRepository.save(user);
            return true;
        }

        return false;

    }

    @Override
    public User getUserInfo(int accountId) {
        var user = new User();
        user.setAccountId(accountId);
        Example<User> example = Example.of(user, ExampleMatcher.matching().withIgnoreNullValues());

        return userRepository.findOne(example).get();
    }

}
