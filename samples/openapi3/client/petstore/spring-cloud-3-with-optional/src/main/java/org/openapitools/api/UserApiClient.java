package org.openapitools.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.openapitools.configuration.ClientConfiguration;

@FeignClient(name="${user.name:user}", contextId="${user.contextId:${user.name}}", url="${user.url:http://petstore.swagger.io/v2}", configuration = ClientConfiguration.class)
public interface UserApiClient extends UserApi {
}
