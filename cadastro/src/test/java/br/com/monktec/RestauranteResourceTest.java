package br.com.monktec;

import br.com.monktec.entity.Restaurante;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
public class RestauranteResourceTest {

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testBuscarRestaurantes() {
        String resultado = given()
                .when().get("/restaurantes")
                .then()
                .statusCode(200)
                .extract().asString();
        Approvals.verify(resultado);
    }

    private RequestSpecification given(){
        return RestAssured.given().contentType(ContentType.JSON);
    }
    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testAlteraRestaurante() {
        Restaurante dto = new Restaurante();
        dto.setNome("novoNome");
        Long paramValue = 123L;
        given()
                .with().pathParam("id", paramValue)
                .body(dto)
                .when().put("/restaurantes/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode())
                .extract().asString();
        Restaurante findById = Restaurante.findById(paramValue);

        Assert.assertEquals(dto.getNome(), findById.getNome());
    }
}
