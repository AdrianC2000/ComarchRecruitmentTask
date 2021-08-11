/* Class handling both databases */

package org.acme;

import Data.ReturnMessage;
import Database.DatabaseHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/{tableName}")
public class UniversalResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecords(@PathParam("tableName") String tableName) {
        ReturnMessage response = DatabaseHandler.getResource(tableName);
        if (response.isValid())
            return Response.ok(response.getResult()).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createResource(@PathParam("tableName") String tableName, Object newObject) {
        ReturnMessage response = DatabaseHandler.addResource(tableName, newObject);
        if (response.isValid())
            return Response.ok(response.getMessage()).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
    }

    @PUT
    @Path("{id}/{parameterToChange}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateResource(
            @PathParam("tableName") String tableName,
            @PathParam("id") Integer id,
            @PathParam("parameterToChange") String param,
            @QueryParam("value") String valueToSet) {
        ReturnMessage response = DatabaseHandler.updateResource(tableName, id, param, valueToSet);
        if (response.isValid()) {
            return Response.ok(response.getMessage()).build();
        }
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteResource(
            @PathParam("tableName") String tableName,
            @PathParam("id") Integer id) {
        ReturnMessage response = DatabaseHandler.deleteResource(tableName, id);
        if (response.isValid())
            return Response.ok(response.getMessage()).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    response.getMessage()).build();
    }

    @POST
    @Path("/filter/{logic}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filterResources(@PathParam("tableName") String tableName, @PathParam("logic") String logic, Object newObject) {
        ReturnMessage response = DatabaseHandler.filterResource(tableName, newObject, logic);
        if (response.isValid())
            return Response.ok(response.getResult()).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
    }
}
