package com.example.cipher.domain.repository.event

import com.example.cipher.domain.models.event.EventResourceSubscription
import com.example.cipher.domain.models.message.Message

interface EventSubscriptionService {
    fun subscribe(subscriptionsAdd: List<EventResourceSubscription>)
    fun unsubscribe(subscriptionsRemove: List<EventResourceSubscription>)
}