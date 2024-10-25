package com.example.cipher.domain.models.user

data class PagerContactRequest (
    val offset: Int,
    val size: Int,
    val contactList: List<User>
)