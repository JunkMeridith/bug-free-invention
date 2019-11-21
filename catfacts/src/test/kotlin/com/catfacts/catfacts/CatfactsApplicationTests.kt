package com.catfacts.catfacts

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        val searchResult = testRestTemplate.getForEntity("/search?animal=cat", FactObject::class.java)
        assertNotNull(searchResult)
        assertEquals(searchResult.statusCode, HttpStatus.OK)

        val fact = searchResult.body

        if (fact != null) {
            assertEquals("cat", fact.type)
            assertNotNull(fact.text)
            assertNotNull(fact._id)
        } else {
            assertTrue(false, "Cat Fact was null")
        }
    }


    @Test
    fun `Calling Search with an Animal type of Dog should return an animal type of dog`() {
        testRestTemplate.getForEntity("/refresh?animal=dog", String::class.java)
        val searchResult = testRestTemplate.getForEntity("/search?animal=dog", FactObject::class.java)
        assertNotNull(searchResult)
        assertEquals(searchResult.statusCode, HttpStatus.OK)

        val fact = searchResult.body

        if (fact != null) {
            assertEquals("dog", fact.type)
            assertNotNull(fact.text)
            assertNotNull(fact._id)
        } else {
            assertTrue(false, "dog Fact was null")
        }
    }

    @Test
    fun `calling refresh on an animal that doesnt exist should return no content response`() {
        val refreshResult = testRestTemplate.getForEntity("/refresh?animal=bldkdfjd", String::class.java)
        assertEquals(HttpStatus.NO_CONTENT, refreshResult.statusCode)
    }

}
