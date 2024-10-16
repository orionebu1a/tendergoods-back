package com.orion.service

import com.orion.entity.Bid
import com.orion.filter.BidFilter
import com.orion.form.BidDto
import com.orion.repository.BidRepository

class BidService(private val bidRepository: BidRepository) {
    fun getAllBids(): List<BidDto> = bidRepository.findAll()

    fun getAllBidsByFilter(filter: BidFilter): List<BidDto> = bidRepository.findAll()
    //TODO

    fun createBid(bidDto: BidDto) = bidRepository.create(bidDto)
    fun getBidById(id: Int): BidDto? = bidRepository.findById(id)

    fun updateBid(id: Int, bid: BidDto): Boolean = bidRepository.update(id, bid)

    fun deleteBid(id: Int): Boolean = bidRepository.delete(id)
}