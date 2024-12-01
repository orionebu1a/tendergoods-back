package com.orion.api

import com.orion.entity.User
import com.orion.service.PromotionService

import com.orion.errors.respondWithErrorProcessing
import com.orion.model.BuyPromotionForm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.promotionRouting(promotionService: PromotionService) {
    route("/promotions") {
        get {
            val promotionTypes = promotionService.findAllPromotionTypes()
            call.respondWithErrorProcessing(promotionTypes)
        }
        post("/buyPromotion") {
            val promotionForm = call.receive<BuyPromotionForm>()
            val user = call.principal<User>()
            val promotion = promotionService.buyPromotion(user!!, promotionForm.id, promotionForm.bidId)
            call.respondWithErrorProcessing(promotion)
        }
    }
}