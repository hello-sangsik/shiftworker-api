package shiftworker.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shiftworker.community.domain.User;
import shiftworker.community.exception.DuplicatedUsernameException;
import shiftworker.community.exception.UnAuthenticationException;
import shiftworker.community.repository.UserRepository;


/**
 * @author sangsik.kim
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(User user) {
        verifyDuplicateUsername(user.getUsername());
        user.hashPassword();
        return userRepository.save(user);
    }

    private void verifyDuplicateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicatedUsernameException();
        }
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(UnAuthenticationException::new);
        if (!user.matchPassword(password)) {
            throw new UnAuthenticationException();
        }
        return user;
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UnAuthenticationException::new);
    }
}

