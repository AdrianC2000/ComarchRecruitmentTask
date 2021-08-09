/* Class handling both databases */

package org.acme;

import Database.DatabaseHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/{tableName}")
public class UniversalResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecords(@PathParam("tableName") String tableName) {
        List<Object> resources = DatabaseHandler.getResource(tableName);
        if (resources != null)
            return Response.ok(resources).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity("Table '" + tableName + "' does not exist.").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createResource(@PathParam("tableName") String tableName, Object newObject) {
        int added = DatabaseHandler.addResource(tableName, newObject);
        switch (added) {
            case 1:
                return Response.ok("Resource added correctly.").build();
            case 2:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        tableName + " table does not exist.").build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        "Incorrect format of data. Please check swagger.com for the correct format of input data.").build();
        }
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
        System.out.println("id = " + id + ", parameterToChange = " + param + ", valueToSet = " + valueToSet);
        int updated = DatabaseHandler.updateResource(tableName, id, param, valueToSet);
        switch(updated) {
            case 1:
                return Response.ok("Parameter " + param + " changed for " + valueToSet + " for " + resourceName(tableName).toLowerCase() + " with the id " + id + " correctly.").build();
            case 2:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        tableName + " table does not exist.").build();
            case 3:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        resourceName(tableName) + " with id " + id + " does not exist.").build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        "Incorrect format of data. Please check swagger.com for the correct format of input data.").build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteResource(
            @PathParam("tableName") String tableName,
            @PathParam("id") Integer id) {
        int removed = DatabaseHandler.deleteResource(tableName, id);
        switch (removed) {
            case 1:
                return Response.ok("User with the id " + id + " deleted correctly.").build();
            case 2:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        "'" + tableName + "' table does not exist.").build();
            case 3:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        resourceName(tableName) +
                                " with the id " + id + " does not exist.").build();
            default:
                return Response.status(Response.Status.BAD_REQUEST).entity(
                        "Incorrect format of data. Please check swagger.com for the correct format of input data.").build();
        }
    }

    private String resourceName(String tableName) {
        String resource = tableName.substring(0, tableName.length() - 1);
        return resource.substring(0, 1).toUpperCase() + resource.substring(1);
    }

    @POST
    @Path("/filter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response filterResources(@PathParam("tableName") String tableName, Object newObject) {

        List<Object> resources = DatabaseHandler.filterResource(tableName, newObject);
        if (resources == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(
                    "Incorrect format of data. Please check swagger.com for the correct format of input data.").build();
        else if (resources.isEmpty())
            return Response.ok("No entries with the given criteria.").build();
        else
            return Response.ok(resources).build();
    }
}
