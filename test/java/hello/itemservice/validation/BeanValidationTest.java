package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setItemName(" ");
        item.setPrice(0);
        item.setQuantity(1000000);

        Set<ConstraintViolation<Item>> validatorSet = validator.validate(item);
        for(ConstraintViolation<Item> valiolation : validatorSet){
            System.out.println(valiolation);
            System.out.println(valiolation.getMessage());
        }
    }

    @Test
    void mainMethod(){
        Map<String, Object > data = new HashMap<>();
        data.put("sss", 123);
        data.put("sssd", LocalDateTime.now());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new Date("Sat Dec 01 00:00:00 GMT 2012");
        String format = sdf.format(date);
        System.out.println(format);

        System.out.println(LocalDateTime.now());

//        String format = sdf.format(LocalDateTime.now().toLocalDate());

    }
}
