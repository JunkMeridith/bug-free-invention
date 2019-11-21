package com.catfacts.catfacts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

@RestController
class Controllers {
    var RestTemplate = RestTemplate()

    @Autowired
    lateinit var factRepository: FactRepository


    @GetMapping("/refresh")
    fun refreshAnimalFacts(@RequestParam animal: String): ResponseEntity<Void> {

        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI.create("https://cat-fact.herokuapp.com/facts/random?animal_type=${animal}&amount=10"))
        val allFacts: ResponseEntity<List<FactObject>> = RestTemplate.exchange(requestEntity, object: ParameterizedTypeReference<List<FactObject>>() {})

        return if (allFacts.body == null  || allFacts.body!!.isEmpty()) {
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        } else {
            factRepository.saveAll(allFacts.body!!)
            ResponseEntity<Void>(HttpStatus.OK)
        }
    }

    @GetMapping("/search")
    fun searchAnimalFacts(@RequestParam animal: String): List<FactObject> {
        return factRepository.findAllByType(animal)
    }

}