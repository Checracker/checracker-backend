package org.checracker.backend.front.util

import java.security.InvalidParameterException

object UserUtil {

    const val nicknameLength = 10

    // nickname
    fun generateRandomNickname(): String {
        return adjectives.random() + " 쿠키"
    }

    fun checkNickname(nickname: String): String {
        if (nickname.length < 2 || nickname.length > 10) {
            throw InvalidParameterException("닉네임은 2글자 이상 10글자 이하여야 합니다.")
        } else {
            return nickname
        }
    }

    private val adjectives = listOf(
        "기쁜",
        "벅찬",
        "포근한",
        "흐뭇한",
        "상쾌한",
        "짜릿한",
        "시원한",
        "반가운",
        "후련한",
        "살맛나는",
        "신바람 나는",
        "아늑한",
        "흥분되는",
        "온화한",
        "안전한",
        "느긋한",
        "끝내주는",
        "날아 갈 듯한",
        "괜찮은",
        "쌈박한",
        "정다운",
        "그리운",
        "화사한",
        "자유로운",
        "따사로운",
        "감미로운",
        "황홀한",
        "상큼한",
        "평화로운",
        "활기찬",
        "힘찬",
        "생생한",
        "의기양양한",
        "든든한",
        "격렬한",
        "열렬한",
        "당당한",
        "팔팔한",
        "엄청난",
        "자신만만한",
        "패기만만한",
        "야생마 같은",
    )
}
