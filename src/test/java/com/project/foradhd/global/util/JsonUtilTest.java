package com.project.foradhd.global.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JsonUtil 테스트")
class JsonUtilTest {

    @DisplayName("String -> Class 변환 테스트")
    @Test
    void convert_string_to_class_test() {
        //given
        String source = """
            {"testInteger": 1, "testString": "테스트"}
            """;

        //when
        TestObject testObject = JsonUtil.readValue(source, TestObject.class);

        //then
        assertThat(testObject.getTestInteger()).isEqualTo(1);
        assertThat(testObject.getTestString()).isEqualTo("테스트");
    }

    @DisplayName("String -> List 변환 테스트")
    @Test
    void convert_string_to_list_test() {
        //given
        String source = """
            [{"testInteger": 1, "testString": "테스트1"},
            {"testInteger": 2, "testString": "테스트2"},
            {"testInteger": 3, "testString": "테스트3"}]
            """;

        //when
        List<TestObject> testObjectList = JsonUtil.readValue(source, new TypeReference<>() {});

        //then
        assertThat(testObjectList).extracting("testInteger", "testString")
            .containsExactly(
                tuple(1, "테스트1"),
                tuple(2, "테스트2"),
                tuple(3, "테스트3"));
    }

    @DisplayName("InputStream -> Class 변환 테스트")
    @Test
    void convert_inputstream_to_class_test() {
        //given
        String source = """
            {"testInteger": 1, "testString": "테스트"}
            """;
        InputStream sourceInputStream = new ByteArrayInputStream(source.getBytes());

        //when
        TestObject testObject = JsonUtil.readValue(sourceInputStream, TestObject.class);

        //then
        assertThat(testObject.getTestInteger()).isEqualTo(1);
        assertThat(testObject.getTestString()).isEqualTo("테스트");
    }

    @DisplayName("Class -> String 변환 테스트")
    @Test
    void convert_class_to_string_test() {
        //given
        TestObject source = new TestObject(1, "테스트1");

        //when
        String testObjectStr = JsonUtil.writeValueAsString(source);

        //then
        assertThat(testObjectStr).isEqualTo("{\"testInteger\":1,\"testString\":\"테스트1\"}");
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestObject {

        private Integer testInteger;
        private String testString;
    }
}
