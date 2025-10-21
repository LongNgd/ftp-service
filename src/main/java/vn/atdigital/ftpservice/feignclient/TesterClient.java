package vn.atdigital.ftpservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.atdigital.ftpservice.configuration.security.ClientFeignConfiguration;

@FeignClient(
        name = "${feign-client.tester-service.name}",
        url = "${feign-client.tester-service.url}",
        path = "${feign-client.tester-service.path}",
        configuration = ClientFeignConfiguration.class
)
public interface TesterClient {

    @GetMapping("/asset-tree/asset-path")
    ResponseEntity<String> getAssetPath(@RequestParam Long assetId, @RequestParam String assetType);
}
