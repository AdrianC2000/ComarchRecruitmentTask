package org.acme;

import Data.User;
import Database.DatabaseHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UsersResource {
    public static List<User> users = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<User> users = DatabaseHandler.getResource("users");
        return Response.ok(users).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUsers(User newUser) {
        boolean added = DatabaseHandler.addResource("users", newUser);
        if (added)
            return Response.ok("User added correctly.").build();
        else
            return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("{id}/{parameterToChange}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(
            @PathParam("id") Integer id,
            @PathParam("parameterToChange") String param,
            @QueryParam("value") String valueToSet) {
        System.out.println("id = " + id + ", parameterToChange = " + param + ", valueToSet = " + valueToSet);
        boolean updated = DatabaseHandler.updateResource("users", id, param, valueToSet);
        if (updated)
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(
            @PathParam("id") Integer id) {
        boolean doesUserExist = DatabaseHandler.userExistence("users", id);
        boolean removed = false;
        if (doesUserExist)
            removed = DatabaseHandler.deleteResource("users", id);
        if (removed)
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }
}
