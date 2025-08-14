package com.back.global.app

import com.back.standard.util.Ut
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AppConfig {
    @Autowired
    fun setEnvironment(environment: Environment) {
        Companion.environment = environment
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Autowired
    fun setObjectMapper(objectMapper: ObjectMapper) {
        Companion.objectMapper = objectMapper
    }

    @PostConstruct
    fun postConstruct() {
        Ut.json.objectMapper = objectMapper
    }

    companion object {
        private lateinit var environment: Environment

        @JvmStatic
        fun isDev(): Boolean = environment.matchesProfiles("dev")

        @JvmStatic
        fun isTest(): Boolean = environment.matchesProfiles("test")

        @JvmStatic
        fun isProd(): Boolean = environment.matchesProfiles("prod")

        @JvmStatic
        fun isNotProd(): Boolean = !isProd()

        private lateinit var objectMapper: ObjectMapper
    }
}
