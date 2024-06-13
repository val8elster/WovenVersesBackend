package com.hsbsose24.vvbackend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    val time1: Float,
    val time2: Float,
    val time3: Float,
    val time4: Float,
    val time5: Float,
    var rank: Int
)