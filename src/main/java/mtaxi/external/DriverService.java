
package mtaxi.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="Driver", url="${api.url.driver}")

public interface DriverService {

    @RequestMapping(method= RequestMethod.GET, path="/drivers/check")
    public void checkOrder(@RequestBody Driver driver);

}