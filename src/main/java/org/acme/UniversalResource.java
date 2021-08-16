/* Class handling both databases */

package org.acme;

import Data.Parsers;
import Data.ReturnMessage;
import Data.Validators;
import Database.DatabaseHandler;
import Database.DatabaseHandlerUser;
import Models.Book;
import Models.User;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;

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


    @Inject
    Validator validator;
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createResource(@PathParam("tableName") String tableName, Object newObject) {
        if (tableName.equals("users")) {
            User newUser = Parsers.parseObjectIntoUser(newObject);
            Set<ConstraintViolation<User>> violations = validator.validate(newUser);
            if (violations.isEmpty()) {
                ReturnMessage response = DatabaseHandler.addResource(tableName, newObject);
                if (response.isValid())
                    return Response.ok(response.getMessage()).build();
                else
                    return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            } else {
                StringBuilder violationsString = new StringBuilder();
                for (ConstraintViolation<User> user : violations) {
                    violationsString.append(" ").append(user.getMessage()).append("\n");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
            }

        } else if (tableName.equals("books")) {
            Book newBook = Parsers.parseObjectIntoBook(newObject);
            Set<ConstraintViolation<Book>> violations = validator.validate(newBook);
            if (violations.isEmpty()) {
                ReturnMessage response = DatabaseHandler.addResource(tableName, newObject);
                if (response.isValid())
                    return Response.ok(response.getMessage()).build();
                else
                    return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            } else {
                StringBuilder violationsString = new StringBuilder();
                for (ConstraintViolation<Book> book : violations) {
                    violationsString.append(" ").append(book.getMessage()).append("\n");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Table '" + tableName + "' does not exist.").build();
    }

    @Inject
    Validator validatorPut;
    @PUT
    @Path("{id}/{parameterToChange}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateResource(
            @PathParam("tableName") String tableName,
            @PathParam("id") Integer id,
            @PathParam("parameterToChange") String param,
            @QueryParam("value") String valueToSet) {

        if (tableName.equals("users")) {

            Set<ConstraintViolation<User>> violations;

            try {
                User newUser = new User();
                Method method = User.class.getDeclaredMethod("set" + param.substring(0, 1).toUpperCase() + param.substring(1), String.class);
                method.invoke(newUser, valueToSet);
                violations = validatorPut.validate(newUser);
            } catch (ParseException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Format error: Wrong parameter name.").build();
            }

            if (violations.isEmpty()) {
                ReturnMessage response = DatabaseHandler.updateResource(tableName, id, param, valueToSet);
                if (response.isValid()) {
                    return Response.ok(response.getMessage()).build();
                } else
                    return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            } else {
                StringBuilder violationsString = new StringBuilder();
                for (ConstraintViolation<User> user : violations) {
                    violationsString.append(" ").append(user.getMessage()).append("\n");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
            }
        }

        else if (tableName.equals("books")) {

            Set<ConstraintViolation<Book>> violations;

            try {
                Book newBook = new Book();
                String methodName = "set" + param.substring(0, 1).toUpperCase() + param.substring(1);
                if (!methodName.contains("setIs_taken")) {
                    Method method = Book.class.getDeclaredMethod(methodName, String.class);
                    method.invoke(newBook, valueToSet);
                }
                if (!methodName.contains("setIs_taken")) {
                    Method method = Book.class.getDeclaredMethod(methodName, String.class);
                    method.invoke(newBook, valueToSet);
                }

                violations = validatorPut.validate(newBook);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Format error: Wrong parameter name.").build();
            }

            if (violations.isEmpty()) {
                ReturnMessage response = DatabaseHandler.updateResource(tableName, id, param, valueToSet);
                if (response.isValid()) {
                    return Response.ok(response.getMessage()).build();
                } else
                    return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
            } else {
                StringBuilder violationsString = new StringBuilder();
                for (ConstraintViolation<Book> book : violations) {
                    violationsString.append(" ").append(book.getMessage()).append("\n");
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(violationsString.toString()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Table '" + tableName + "' does not exist.").build();
    }


    @DELETE
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
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
            if (response.getResult().isEmpty())
                return Response.ok("There are not any records fulfilling your requirements. " +
                        "Check your input for typos or change your requirements.").build();
            else
                return Response.ok(response.getResult()).build();
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(response.getMessage()).build();
    }
}
