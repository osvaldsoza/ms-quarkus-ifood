package br.com.monktec.resource;

import br.com.monktec.entity.Prato;
import br.com.monktec.entity.PratoDTO;
import br.com.monktec.entity.Restaurante;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurantes")
public class RestauranteResource {

    @GET
    public List<Restaurante> listAll() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante restauranteDTO) {
        restauranteDTO.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante restauranteDTO) {
        Optional<Restaurante> restauranteOptional = restauranteDTO.findByIdOptional(id);

        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteOptional.get();
        restaurante.setNome(restauranteDTO.getNome());

        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);

        restauranteOptional.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }

    @GET
    @Path("{idRestaurante}/pratos")
    @Tag(name = "pratos")
    public List<Restaurante> listaTodosPratos(@PathParam("idRestaurante") Long idRestaurante) {
        var restauranteOptional = buscarRestaurante(idRestaurante);

        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Path("{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "pratos")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, PratoDTO pratoDTO) {
        var restauranteOptional = buscarRestaurante(idRestaurante);

        var prato = new Prato();
        prato.setNome(pratoDTO.getNome());
        prato.setDescricao(pratoDTO.getDescricao());
        prato.setPreco(pratoDTO.getPreco());
        prato.setRestaurante(restauranteOptional.get());

        prato.persist();

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "pratos")
    public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id, PratoDTO pratoDTO) {
        buscarRestaurante(idRestaurante);

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não encontrado");
        }

        Prato prato = pratoOptional.get();
        prato.setPreco(pratoDTO.getPreco());

        prato.persist();
    }

    @DELETE
    @Path("{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "pratos")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
        buscarRestaurante(idRestaurante);

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        pratoOptional.ifPresentOrElse(Prato::delete, () ->{
            throw new NotFoundException("Prato não encontrado");
        });
    }

    private Optional<Restaurante> buscarRestaurante(Long idRestaurante) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(idRestaurante);

        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        return restauranteOptional;
    }
}
