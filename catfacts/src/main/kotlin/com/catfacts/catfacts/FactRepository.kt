package com.catfacts.catfacts

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FactRepository : JpaRepository<FactObject, String> {
    override fun findAll(): List<FactObject>
}