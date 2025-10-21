package vn.atdigital.ftpservice.configuration.security;

import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Configuration
@ConditionalOnClass(Feign.class)
@EnableFeignClients(basePackages = "vn.atdigital.ftpservice.feignclient")
public class ClientFeignConfiguration {

    /**
     * Interceptor thêm header Accept-Language
     */
    @Bean
    RequestInterceptor languageRequestInterceptor() {
        return requestTemplate ->
                requestTemplate.header("Accept-Language", LocaleContextHolder.getLocale().toLanguageTag());
    }

    /**
     * Interceptor thêm OAuth2 token cho tất cả Feign request (client_credentials)
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.client.registration", name = "ftp_client")
    RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("ftp_client")
                    .principal("feign-client")
                    .build();

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            if (authorizedClient != null) {
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                requestTemplate.header("Authorization", "Bearer " + accessToken.getTokenValue());
            }
        };
    }

    /**
     * Bean quản lý OAuth2 client và token
     */
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService
                );
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }
}
