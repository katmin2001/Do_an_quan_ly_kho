package com.kenzy.manage.do_an_quan_ly_kho.config;

import com.kenzy.manage.do_an_quan_ly_kho.entity.UserEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.Role;
import com.kenzy.manage.do_an_quan_ly_kho.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitialDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Kiểm tra xem có tài khoản admin nào trong cơ sở dữ liệu chưa
        if (!userRepository.existsUserByUsername("admin")) {
            // Tạo tài khoản admin nếu chưa tồn tại
            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("12345678a"))
                    .email("admin@example.com")
                    .name("Administrator")
                    .phone("123456789")
                    .address("Admin Address")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
