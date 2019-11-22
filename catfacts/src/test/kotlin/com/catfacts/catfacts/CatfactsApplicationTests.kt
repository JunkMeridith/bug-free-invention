package com.catfacts.catfacts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI


@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")

class CatfactsApplicationTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate


    companion object {
        @Container
        var postgresqlContainer = KGenericContainer("postgres")
    }

    class KGenericContainer(imageName: String) : PostgreSQLContainer<KGenericContainer>(imageName)


    @Test
    fun `Calling refresh for cat should return OK status`() {
        val result = testRestTemplate.getForEntity("/refresh?animal=cat", String::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
    }

    @Test
    fun `Calling search after calling cat fact should add cat fact to database`() {
        testRestTemplate.getForEntity("/refresh?animal=cat", String::class.java)

        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI.create("/search?animal=cat"))
        val searchResult: ResponseEntity<List<FactObject>> = testRestTemplate.exchange(requestEntity, object: ParameterizedTypeReference<List<FactObject>>() {})

        assertNotNull(searchResult)
        assertEquals(searchResult.statusCode, HttpStatus.OK)

        val fact = searchResult.body

        if (fact != null) {
            var firstCat = fact[0]
            assertEquals("cat", firstCat.type)
            assertNotNull(firstCat.text)
            assertNotNull(firstCat._id)
        } else {
            assertTrue(false, "Cat Fact was null")
        }
    }

    @Test
    fun `Calling Search with an Animal type of Dog should return a list of dogs`() {
        testRestTemplate.getForEntity("/refresh?animal=dog", String::class.java)
        val requestEntity = RequestEntity<Any>(HttpMethod.GET, URI.create("/search?animal=dog"))
        val searchResult: ResponseEntity<List<FactObject>> = testRestTemplate.exchange(requestEntity, object: ParameterizedTypeReference<List<FactObject>>() {})
        assertNotNull(searchResult)
        assertEquals(searchResult.statusCode, HttpStatus.OK)

        val fact = searchResult.body

        if (fact != null) {
            assertEquals(10, fact.size)
            val firstDog = fact[0]
            assertEquals("dog", firstDog.type)
            assertNotNull(firstDog.text)
            assertNotNull(firstDog._id)
        } else {
            assertTrue(false, "List of Dog Facts was null")
        }
    }

    @Test
    fun `calling refresh on an animal that doesnt exist should return no content response`() {
        val refreshResult = testRestTemplate.getForEntity("/refresh?animal=bldkdfjd", String::class.java)
        assertEquals(HttpStatus.NO_CONTENT, refreshResult.statusCode)
    }

}
