package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    /*
    * 해당 버전의 validation 처리는 type 오류 처리가 안된다.
    * 'Item' 의 price, quantity 같은 숫자 필드 타입이 Integer이므로 문자타입으로 설정하는 것이 불가능한데,
    * 문자타입이 들어오면 오류가 발생한다.
    * 해당 오류는 컨트롤러에 진입전에 예외가 발생되어 컨트롤러가 제대로 호출되지 않는다.
    * */

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품이름을 필수 입니다");
        }

        if(item.getPrice()==null || item.getPrice()<1000 || item.getPrice() > 1000000){
            errors.put("price","가격은 1000~1000000 까지 허용합니다" );
        }

        if(item.getQuantity()==null || item.getQuantity() >= 9999){
            errors.put("quantity", "수량은 최대 9999개 까지입니다");
        }

        if(item.getPrice()!= null && item.getQuantity() != null){
            int resultPrices = item.getPrice() * item.getQuantity();
            if(resultPrices<10000){
                errors.put("globalError", "가격 * 수량 합은 10000원 이상이어야합니다.  현재값 "+ resultPrices);
            }
        }

        //검증에 실패하면 다시 입력폼으로
        if(!errors.isEmpty()){
            log.info("errors = {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }


        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

