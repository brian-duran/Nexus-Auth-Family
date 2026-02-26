package userapp.brian.duran.userappapi.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import userapp.brian.duran.userappapi.annotation.LogExecutionTime;
import userapp.brian.duran.userappapi.entity.User;
import userapp.brian.duran.userappapi.notification.event.UserCreatedEvent;
import userapp.brian.duran.userappapi.service.EmailService;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventListener {

    private final EmailService emailService;

    @Async("taskExecutor")
    @LogExecutionTime
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserCreated(UserCreatedEvent event) {

        User user = event.user();

        log.info("🧵 [Hilo: {}] 📨 Recibido evento de usuario: {}. Enviando email...",
                Thread.currentThread().getName(), user.getUsername());

        String subject = "¡Bienvenido a la plataforma, " + user.getUsername() + "!";

        String body = """
                <h1>Hola %s</h1>
                <hr/>
                <p style='color:red'>Tu registro ha sido exitoso.</p>
                <p>Ya puedes iniciar sesión.</p>
                <button style='background-color: blue;color:white;'"'>Presiona Aquí</button>
                """.formatted(user.getUsername());

        emailService.sendWelcomeEmail(user.getEmail(), subject, body);

        log.info("✅ [Hilo: {}] 📨 Email enviado.", Thread.currentThread().getName());
    }
}
