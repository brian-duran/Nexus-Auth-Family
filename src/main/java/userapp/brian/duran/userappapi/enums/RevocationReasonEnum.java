package userapp.brian.duran.userappapi.enums;

public enum RevocationReasonEnum {
    LOGOUT,
    ADMIN_REVOKE, // Un admin bloqueó al usuario desde el panel
    SECURITY_POLICY, // Cambio de contraseña obligatorio
    THEFT_REPORT, // El usuario reportó robo de dispositivo
    FRAUD_DETECTED, // El sistema detectó una IP sospechosa
    ROTATED,
    NOT_REVOKED // Aún activo (valor por defecto si revoked=false)
}
