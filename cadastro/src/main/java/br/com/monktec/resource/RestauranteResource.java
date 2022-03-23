package br.com.monktec.resource;

import br.com.monktec.entity.Restaurante;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @GET
    public List<Restaurante> listAll() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante restauranteDTO){
        restauranteDTO.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante restauranteDTO){
        Optional<Restaurante> restauranteOptional = restauranteDTO.findByIdOptional(id);

        if(restauranteOptional.isEmpty()){
            throw new NotFoundException();
        }

        Restaurante restaurante = restauranteOptional.get();
        restaurante.nome = restauranteDTO.nome;

        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id){
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);

        restauranteOptional.ifPresentOrElse(Restaurante::delete, () ->{
            throw new NotFoundException();
        });
    }
}
