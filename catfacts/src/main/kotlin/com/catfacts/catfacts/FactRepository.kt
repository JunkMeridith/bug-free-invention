package com.catfacts.catfacts

import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface FactRepository : JpaRepository<FactObject, String> {
    override fun findAll(): List<FactObject>
}