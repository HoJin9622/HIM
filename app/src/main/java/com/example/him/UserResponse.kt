package com.example.him

// 회원 Entity 클래스
data class UserResponse (
    val _id: String,  // Primary Key
    val userId: String,  // 아이디
    val password: String,  // 비밀번호
    val name: String,  // 이름
    val isProvider: Boolean  // 가입 형태(공급자 or 소비자)
)