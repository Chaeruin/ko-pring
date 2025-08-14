package com.back.domain.post.post.controller

import com.back.domain.member.member.service.MemberService
import com.back.domain.post.post.dto.PostDto
import com.back.domain.post.post.dto.PostWithContentDto
import com.back.domain.post.post.entity.Post
import com.back.domain.post.post.service.PostService
import com.back.global.rq.Rq
import com.back.global.rsData.RsData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.RequiredArgsConstructor
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "ApiV1PostController", description = "API 글 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
class ApiV1PostController (
    private val postService: PostService,
    private val memberService: MemberService,
    private val rq: Rq
) {
    @get:Operation(summary = "다건 조회")
    @get:Transactional(readOnly = true)
    @get:GetMapping
    val items: List<PostDto>
        get() {
            val items = postService.findAll()

            return items
                .stream()
                .map<PostDto> { post: Post -> PostDto(post) }  // PostDto로 변환
                .toList()
        }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건 조회")
    fun getItem(@PathVariable id: Int): PostWithContentDto {
        val post = postService.findById(id)

        return PostWithContentDto(post)
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    fun delete(
        @PathVariable id: Int
    ): RsData<Void> {
        val actor = rq.actor

        val post = postService.findById(id)

        post.checkActorCanDelete(actor)

        postService.delete(post)

        return RsData<Void>(
            "200-1",
            "${id}번 글이 삭제되었습니다."
        )
    }

    data class PostWriteReqBody(
        val title: @NotBlank @Size(min = 2, max = 100) String,
        val content: @NotBlank @Size(min = 2, max = 5000) String
    )

    @PostMapping
    @Transactional
    @Operation(summary = "작성")
    fun write(
        @RequestBody reqBody: @Valid PostWriteReqBody
    ): RsData<PostDto> {
        val actor = rq.actor

        val post = postService.write(actor, reqBody.title, reqBody.content)

        return RsData<PostDto>(
            "201-1",
            "${post.id}번 글이 작성되었습니다.",
            PostDto(post)
        )
    }

    data class PostModifyReqBody(
        val title: @NotBlank @Size(min = 2, max = 100) String,
        val content: @NotBlank @Size(min = 2, max = 5000) String
    )

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    fun modify(
        @PathVariable id: Int,
        @RequestBody reqBody: @Valid PostModifyReqBody
    ): RsData<Void> {
        val actor = rq.actor

        val post = postService.findById(id)

        post.checkActorCanModify(actor)

        postService.modify(post, reqBody.title, reqBody.content)

        return RsData<Void>(
            "200-1",
            "${post.id}번 글이 수정되었습니다."
        )
    }
}