package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    /**
     * bindingResult 는 V1에서 컨트롤러가 호출되지 않는 오류 (TypeMismatch 등) 정보를 bindingResult에 담아서 컨트롤러에 정상적으로 호출한다.
     * bindingResult 대신 Errors 를 사용하여도 되나, 편의 메소드가 적다.
     */

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder // Controller 에서만 적용이 된다.
    public void init(WebDataBinder dataBinder){
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();
        ;

        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", "상품이름은 필수 입니다"));
        }

        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", "가격은 1000~1000000까지 허용합니다"));
        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "qauntity", "최대수량은 9999입니다"));
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                bindingResult.addError(new ObjectError("item", "가격 수량의 합은 1000000입니다"));
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }



        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();
        ;



        if(!StringUtils.hasText(item.getItemName())){

            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수 입니다"));

        }

        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1000~1000000까지 허용합니다"));

        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "qauntity", item.getQuantity(),false, null, null, "최대수량은 9999입니다"));
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                bindingResult.addError(new ObjectError("item", null, null,"가격 수량의 합은 1000000입니다"));
            }
        }



        //검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }



        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();

        if(!StringUtils.hasText(item.getItemName())){
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000,100000}, null));
        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrices},null));
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//      위 코드와 같이 동작한다.
//        if(!StringUtils.hasText(item.getItemName())){
//            bindingResult.rejectValue("itemName","required");
//        }





        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            bindingResult.rejectValue("price","range", new Object[]{10000, 100000}, null);
        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrices}, null);
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();

        itemValidator.validate(item, bindingResult);

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add") //Databinder 와 연결이 되게끔 @Validated 추가한다.
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();

        itemValidator.validate(item, bindingResult);

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

