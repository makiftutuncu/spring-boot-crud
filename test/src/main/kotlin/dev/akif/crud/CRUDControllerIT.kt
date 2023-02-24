package dev.akif.crud

import com.fasterxml.jackson.databind.ObjectMapper
import dev.akif.crud.config.MySqlDBTestContext
import io.restassured.RestAssured
import io.restassured.internal.common.assertion.Assertion
import io.restassured.response.Response
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.test.context.ContextConfiguration
import java.io.Serializable

@ContextConfiguration(initializers = [MySqlDBTestContext::class])
abstract class CRUDControllerIT<
        I : Serializable,
        E : CRUDCreateDTO,
        D : CRUDDTO<I>,
        Path : String,
        TestData : CRUDTestDto<I, E, D>> (protected val testData: TestData, protected val baseUrl: Path){

    private val mapper = ObjectMapper()


    @Test
    fun testCreateAlreadyExists() {
        val response = post();
        val expected =  testData.testDto
        assertEquals(201, response.statusCode)
        val actual = mapper.readValue(response.body.asString(), CRUDDTO::class.java)
        assertEquals(expected, actual)
    }

    fun post(): Response {
        val body = mapper.writeValueAsString(testData.testCreateDto)
        return RestAssured.given().body(body).post(baseUrl)
    }
}