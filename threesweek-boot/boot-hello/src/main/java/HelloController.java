import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping(value = "/hello")
    public void getHello(){
        System.out.println("hello word");
    }

}
