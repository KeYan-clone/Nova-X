package com.novax.common.swagger.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * OpenAPI/Swagger 配置
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@RequiredArgsConstructor
public class OpenApiConfig {

    private final SwaggerProperties properties;

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion());

        if (StringUtils.hasText(properties.getTermsOfService())) {
            info.setTermsOfService(properties.getTermsOfService());
        }

        if (StringUtils.hasText(properties.getContactName())
                || StringUtils.hasText(properties.getContactEmail())
                || StringUtils.hasText(properties.getContactUrl())) {
            Contact contact = new Contact()
                    .name(properties.getContactName())
                    .email(properties.getContactEmail())
                    .url(properties.getContactUrl());
            info.setContact(contact);
        }

        if (StringUtils.hasText(properties.getLicenseName()) || StringUtils.hasText(properties.getLicenseUrl())) {
            License license = new License()
                    .name(properties.getLicenseName())
                    .url(properties.getLicenseUrl());
            info.setLicense(license);
        }

        OpenAPI openAPI = new OpenAPI().info(info);

        if (StringUtils.hasText(properties.getServerUrl())) {
            openAPI.addServersItem(new Server().url(properties.getServerUrl()));
        }

        if (properties.isBearerEnabled()) {
            String schemeName = properties.getAuthHeaderName();
            SecurityScheme securityScheme = new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name(properties.getAuthHeaderName())
                    .description(properties.getAuthHeaderDescription());
            openAPI.components(new Components().addSecuritySchemes(schemeName, securityScheme));
            openAPI.addSecurityItem(new SecurityRequirement().addList(schemeName));
        }

        return openAPI;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder()
                .group(properties.getGroupName())
                .pathsToMatch(properties.getPathsToMatch().toArray(new String[0]));

        if (!CollectionUtils.isEmpty(properties.getPackagesToScan())) {
            builder.packagesToScan(properties.getPackagesToScan().toArray(new String[0]));
        }

        return builder.build();
    }
}
