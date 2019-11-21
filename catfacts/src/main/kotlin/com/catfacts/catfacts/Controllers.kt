package com.catfacts.catfacts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class Controllers {
    var RestTemplate = RestTemplate()

    @Autowired
    lateinit var factRepository: FactRepository


    @GetMapping("/refresh/cat")
    @ResponseStatus(HttpStatus.OK)
    fun refreshCatFacts() {
        val allfacts = RestTemplate.getForEntity("https://cat-fact.herokuapp.com/facts/random?animal_type=cat&amount=1", FactObject::class.java)

        factRepository.save(allfacts.body!!)
    }

    @GetMapping("/search")
    fun getCatFacts(): FactObject? {
        return factRepository.findAll().getOrNull(0)
    }

}