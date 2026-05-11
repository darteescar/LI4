package app.IecoRideCA.controllers.users.dto;

public record PasswordChangeRequest(
     String newPassword,
     String currentPassword
) {

}
