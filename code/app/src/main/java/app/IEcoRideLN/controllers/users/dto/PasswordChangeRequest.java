package app.IEcoRideLN.controllers.users.dto;

public record PasswordChangeRequest(
     String newPassword,
     String currentPassword
) {}
