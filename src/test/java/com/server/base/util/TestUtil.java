package com.server.base.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class TestUtil {
    /**
     * <h3> JSONPath 사용법 </h3>
     * <pre>
     *  기본 문법 요소
     * JSON의 데이터는 키-값 쌍으로 이루어진 요소들을 객체(object; {}로 표현) 또는 배열(array; []로 표현)로 묶어놓은 형태를 가진다.
     * 데이터 구조가 간단하다 보니 이를 처리하는 JSONPath의 기본 문법 요소도 비교적 간단하게 구성되어 있다. 자주 쓰이는 문법 요소들은 다음과 같다.
     * $ : 루트 노드. JSONPath의 모든 표현식은 이것으로 시작된다.
     * @ : 현재 노드. 아래에서 소개할 조건부 필터 표현식에서 사용된다.
     * . : 하위 노드
     * .. : 중첩된 전체 하위 요소들(nested descendants)
     * [] : 배열 인덱스
     * * : 모든 요소와 매칭되는 와일드 카드
     * ?(boolean expression) : 조건부 필터 표현식
     * </pre>
     */


    public static MockMultipartFile getMockMultiPartFile(String imageFilePath, String type, String multipartFileName) {
        String extension = imageFilePath.substring(imageFilePath.lastIndexOf(".") + 1);


        try {
            InputStream inputStream = new FileInputStream(imageFilePath);
            return new MockMultipartFile(
                    multipartFileName,
                    String.format("%s.%s", multipartFileName, extension),
                    String.format("%s/%s", type, extension),
                    inputStream
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> void injectProperties(Class<T> targetClass, String... profiles) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T target = targetClass.getConstructor().newInstance();

        Arrays.stream(profiles).forEach( profile -> {
            try {
                ClassPathResource resource = new ClassPathResource("application.yml");
                Map<String, Map<String,Object>> yml = new Yaml().load(resource.getInputStream());
                Map<String,Object> constants = yml.get("const");
                inject(target, constants);
            } catch (Exception e){
                throw new RuntimeException();
            }
        });
    }

    private static <T> void inject(T target, Map<String, Object> constants){
        constants.keySet().forEach(key -> {
            String k = key.substring(0, 1).toUpperCase()+key.substring(1);
            if(constants.get(key) instanceof  Map){
                inject(target, (Map<String, Object>) constants.get(key));
            } else {
                try {
                    ReflectionTestUtils.invokeSetterMethod(target, k, constants.get(key));
                } catch (Exception e) {
                }
            }
        });
    }
}
