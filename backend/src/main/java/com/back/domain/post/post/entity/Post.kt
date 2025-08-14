package com.back.domain.post.post.entity

import com.back.domain.member.member.entity.Member
import com.back.domain.post.postComment.entity.PostComment
import com.back.global.exception.ServiceException
import com.back.global.jpa.entity.BaseEntity
import jakarta.persistence.*
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Entity
class Post: BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var author: Member

    @Column(length = 100)
    lateinit var title: String

    @Column(columnDefinition = "LONGTEXT")
    lateinit var content: String

    @OneToMany(
        mappedBy = "post",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    private val comments: MutableList<PostComment> = ArrayList<PostComment>()

    fun modify(title: String, content: String) {
        this.title = title
        this.content = content
    }

    fun addComment(author: Member, content: String): PostComment {
        val postComment = PostComment(author, this, content)
        comments.add(postComment)

        return postComment
    }

    fun findCommentById(id: Int): Optional<PostComment?> {
        return comments
            .stream()
            .filter { comment: PostComment -> comment.id == id }
            .findFirst()
    }

    fun deleteComment(postComment: PostComment): Boolean {
        return comments.remove(postComment)
    }

    fun checkActorCanModify(actor: Member) {
        if (author != actor) throw ServiceException("403-1", "${getId()}번 글 수정권한이 없습니다.")
    }

    fun checkActorCanDelete(actor: Member) {
        if (author != actor) throw ServiceException("403-2", "${getId()}번 글 삭제권한이 없습니다.")
    }
}