package com.project.foradhd.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("Jasypt 학습 테스트")
@TestPropertySource(properties = "jasypt.encryptor.key=jasypt-test-key") //테스트 위한 key 값 설정
@Import(JasyptConfig.class)
@ExtendWith(SpringExtension.class)
class JasyptConfigTest {

    @Autowired
    PBEStringEncryptor encryptor;

    @DisplayName("Jasypt 이용한 (대칭키) 암호화 및 복호화 테스트")
    @Test
    void jasypt_encryption_decryption_test() {
        //given
        String plainText = "I'm doing a Jasypt study test";

        //when
        String encryptedText = encryptor.encrypt(plainText);

        //then
        String decryptedText = encryptor.decrypt(encryptedText);
        assertThat(decryptedText).isEqualTo(plainText);
    }
}
