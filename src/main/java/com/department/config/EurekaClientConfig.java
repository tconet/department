package com.department.config;

import com.department.constants.ProfileConstants;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableEurekaClient
@Profile(ProfileConstants.SPRING_PROFILE_EUREKA)
public class EurekaClientConfig {
}
