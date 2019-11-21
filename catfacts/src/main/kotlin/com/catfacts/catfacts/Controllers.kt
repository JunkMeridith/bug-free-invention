package com.catfacts.catfacts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class Controllers {
    var RestTemplate = RestTemplate()

    @Autowired
    lateinit var factRepository: FactRepository


    @GetMapping("/refresh")
    fun refreshCatFacts(@RequestParam animal: String): ResponseEntity<Void> {
        val allfacts = RestTemplate.getForEntity("https://cat-fact.herokuapp.com/facts/random?animal_type=" + animal + "&amount=1", FactObject::class.java)
        return if (allfacts.body != null) {
            factRepository.save(allfacts.body!!)
            ResponseEntity<Void>(HttpStatus.OK)
        } else {
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        }
    }

    @GetMapping("/search")
    fun getCatFacts(@RequestParam animal: String): FactObject? {
        return factRepository.findAllByType(animal).getOrNull(0)
    }

}