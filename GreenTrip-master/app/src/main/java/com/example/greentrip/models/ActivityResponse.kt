package com.example.greentrip.models

data class ActivityResponse(
    val `data`: List<Data>?,
    val result: Int?,
    val status: String?
) {
    data class Data(
        val __v: Int?,
        val _id: String?,
        val description: String?,
        val id: String?,
        val name: String?,
        val pointOfInterest: PointOfInterest?,
        val reservationLimit: Int?,
        val startAt: String?
    ) {
        data class PointOfInterest(
            val _id: String?,
            val address: String?,
            val agent: Agent?,
            val availableTickets: Int?,
            val costPoints: String?,
            val description: String?,
            val id: String?,
            val name: String?,
            val photo: String?,
            val qrcode: String?,
            val slug: String?
        ) {
            data class Agent(
                val _id: String?,
                val email: String?,
                val id: String?,
                val name: String?,
                val phone: String?
            )
        }
    }
}