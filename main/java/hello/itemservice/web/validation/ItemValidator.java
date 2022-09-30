package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item == clazz
        //item == subItem
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");
//      위 코드와 같이 동작한다.
//        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.rejectValue("itemName","required");
//        }

        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            errors.rejectValue("price","range", new Object[]{10000, 100000}, null);
        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                errors.reject("totalPriceMin", new Object[]{10000, resultPrices}, null);
            }
        }
    }
}
