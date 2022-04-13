package com.dalcho.adme;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith( SpringExtension. class )
@SpringBootTest ( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
class AdmeApplicationTests {

    @Test
    void contextLoads() {
    }

}
