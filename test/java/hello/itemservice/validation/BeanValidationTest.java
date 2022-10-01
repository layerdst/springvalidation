package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.*;
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
}
