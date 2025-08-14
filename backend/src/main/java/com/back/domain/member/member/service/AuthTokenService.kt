package com.back.domain.member.member.service

import com.back.domain.member.member.entity.Member
import com.back.standard.util.Ut
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Map

@Service
class AuthTokenService (
    @Value("\${custom.jwt.secretKey}")
    private val jwtSecretKey: String,
    @Value("\${custom.accessToken.expirationSeconds}")
    private val accessTokenExpirationSeconds: Long
) {
    fun genAccessToken(member: Member): String {
        val id = member.id.toLong()
        val username: String = member.username
        val nickname = member.nickname

        return Ut.jwt.toString(
            jwtSecretKey,
            accessTokenExpirationSeconds,
            Map.of(
                "id", id,
                "username", username,
                "nickname", nickname,
                "authorities", member.authoritiesAsStringList)
        )
    }

    fun payload(accessToken: String): Map<String, Any> {
        val parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken) 
            ?: throw RuntimeException("Invalid JWT token")

        val id = parsedPayload.get("id") as Int
        val username = parsedPayload.get("username") as String
        val nickname = parsedPayload.get("nickname") as String
        val authorities = parsedPayload["authorities"] as List<String>
        
        return mapOf(
            "id" to id,
            "username" to username,
            "nickname" to nickname,
            "authorities" to authorities
        ) as Map<String, Any>
    }
}