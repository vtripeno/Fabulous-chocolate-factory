package br.com.store.core

import org.springframework.stereotype.Service

@Service
class KitchenService {
    fun heatMixture(): String {
        return "Heating the mixture..."
    }

    fun pourIntoMolds(): String {
        return "Pouring into molds..."
    }

    fun coolDown(): String {
        return "Cooling down..."
    }
}