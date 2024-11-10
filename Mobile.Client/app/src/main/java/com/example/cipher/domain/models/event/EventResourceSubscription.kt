package com.example.cipher.domain.models.event

data class EventResourceSubscription(
    val resourceName: String,
    val type: EventSubscriptionType,
    val callback: (Any?) -> Unit
)