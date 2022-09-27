package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

public class MessageCodeResolver {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){
        String[] mes = codesResolver.resolveMessageCodes("required", "item");
        for (String me : mes) {
            System.out.println(me);

        }
        Assertions.assertThat(mes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField(){
        String[] strings = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messages : strings){
            System.out.println(messages);
        }
        Assertions.assertThat(strings).containsExactly("required.item.itemName",
                "required.itemName"
                ,"required.java.lang.String"
                ,"required");
    }

}
