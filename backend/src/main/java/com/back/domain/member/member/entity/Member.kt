package com.back.domain.member.member.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

@Entity
class Member (
    id: Int,
    @field:Column(unique = true) val username: String,
    var password: String? = null,
    var nickname: String,
    @field:Column(unique = true) var apiKey: String,
    var profileImgUrl: String? = null,
) : BaseEntity(id) {

    val name: String
        get() = nickname

    val isAdmin: Boolean
        get() = "admin" == username

    constructor(id: Int, username: String, nickname: String) : this(
        id,
        username,
        null,
        nickname,
        ""
    )

    constructor(username: String, password: String?, nickname: String, profileImgUrl: String?) : this(
        0,
        username,
        password,
        nickname,
        UUID.randomUUID().toString(), // apiKey는 UUID로 생성
        profileImgUrl
    )

    fun modifyApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    val authorities: Collection<GrantedAuthority>
        get() = this.authoritiesAsStringList
            .stream()
            .map { SimpleGrantedAuthority(it) }
            .toList()

    val authoritiesAsStringList: List<String>
        get() {
            val authorities: MutableList<String> = ArrayList<String>()

            if (this.isAdmin) authorities.add("ROLE_ADMIN")

            return authorities
        }

    fun modify(nickname: String, profileImgUrl: String) {
        this.nickname = nickname
        this.profileImgUrl = profileImgUrl
    }

    val profileImgUrlOrDefault: String
        get() {
            profileImgUrl?.let { return it }

            return "https://placehold.co/600x600?text=U_U"
        }
}
