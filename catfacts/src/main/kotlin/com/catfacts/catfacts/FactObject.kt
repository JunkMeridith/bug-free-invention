package com.catfacts.catfacts

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class FactObject(
        val used: Boolean = false,
        val source: String = "unknown",
        val type: String = "unknown",
        val deleted: Boolean = false,
        @Id val _id: String = "unknown",
        val updatedAt: String = "unknown",
        val createdAt: String = "unknown",
        @Column(name = "userName") val user: String = "unknown",
        val text: String = "unknown",
        val __v: Int) {

    constructor() : this(
            false, "unknown", "unknown", false, "a",
            "a", "a", "a", "a", 0)

}