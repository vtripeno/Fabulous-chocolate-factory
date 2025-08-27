package br.com.store.core

import org.springframework.stereotype.Service

@Service
class PostKitchenService {
    fun packaging(): String {
        return "Packaging chocolates..."
    }

    fun completed(): String {
        return "Chocolate production process completed!"
    }
}