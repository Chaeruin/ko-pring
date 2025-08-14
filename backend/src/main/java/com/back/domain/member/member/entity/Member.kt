package com.back.domain.member.member.entity

import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

@Entity
class Member : BaseEntity {
    @Column(unique = true)
    private var username: String

    lateinit var password: String

    lateinit var nickname: String

    @Column(unique = true)
    lateinit var apiKey: String

    lateinit var profileImgUrl: String

    val name: String
        get() = nickname

    val isAdmin: Boolean
        get() = "admin" == username

    constructor(id: Int, username: String, nickname: String) {
        setId(id)
        this.username = username
        this.nickname = nickname
    }

    constructor(username: String, password: String, nickname: String, profileImgUrl: String, apiKey: String) {
        this.username = username
        this.password = password
        this.nickname = nickname
        this.profileImgUrl = profileImgUrl
        this.apiKey = apiKey
    }

    fun modifyApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    val authorities: Collection<GrantedAuthority>
        get() = this.authoritiesAsStringList
            .stream()
            .map { SimpleGrantedAuthority(it) }
            .toList()

    private val authoritiesAsStringList: List<String>
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
            if (profileImgUrl == null) return "https://placehold.co/600x600?text=U_U"

            return profileImgUrl
        }
}
