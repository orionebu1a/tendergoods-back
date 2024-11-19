package com.orion.api

import com.orion.service.PromotionService

import com.orion.errors.respondWithErrorProcessing
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.promotionRouting(promotionService: PromotionService) {
    route("/review") {
        get {
            val promotionTypes = promotionService.findAllPromotionTypes()
            call.respondWithErrorProcessing(promotionTypes)
        }
        post("/buyPromotion") {

        }
    }
}