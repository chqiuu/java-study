package com.chqiuu.study.hibernate.validator.example;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScriptAssertValidatorTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void minLengthIsError() {
        User user = new User();
        user.setFullName("YourBatman");
        user.setName("YourBatman");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
        assertEquals("长度需要在12和2147483647之间", violations.iterator().next().getMessage());
    }

    @Test
    public void nameNotEqualFullName() {
        User user = new User();
        user.setFullName("YourBatmanYourBatmanYourBatman");
        user.setName("YourBatman");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
        assertEquals(1, violations.size());
        assertEquals("name != fullName", violations.iterator().next().getMessage());
    }

    @Test
    public void validateAdulthoodTest() {
        User user = new User();
        user.setFullName("YourBatmanYourBat");
        user.setName("YourBatmanYourBat");
        user.setAge(22);
        user.setIsAdulthood(true);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    /**
     * @ScriptAssert 使用注意事项：脚本中不能出现return，比如”if (1 < 2) {return false} else {return true}”是不可以的，
     * 会报”jdk.nashorn.internal.runtime.ParserException: :1:12 Invalid return statement”，
     * 而写成”if (1 < 2) {true} else {false}”是ok的，
     * 所以大概使用三元运算符会好一些。
     */
    @ScriptAssert.List({
            @ScriptAssert(script = "_this.name==_this.fullName", lang = "javascript", message = "name != fullName"),
            @ScriptAssert(script = "_this.validateAdulthood(_this.age,_this.isAdulthood)", lang = "javascript", message = "年龄不相符11"),
            @ScriptAssert(script = "_this.age > 18 && _this.isAdulthood", lang = "javascript", message = "年龄不相符22")
    })
    @Data
    public class User {
        @NotNull
        private String name;
        @Length(min = 12)
        @NotNull
        private String fullName;
        /**
         * 年龄
         */
        @Max(value = 100, message = "年龄不能超过{value}")
        private Integer age;
        /**
         * 是否成年
         */
        private Boolean isAdulthood;

        public boolean validateAdulthood(Integer age, boolean isAdulthood) {
            if (null == age) {
                return false;
            }
            if (age < 18 && isAdulthood) {
                return false;
            }
            return true;
        }
    }
}

