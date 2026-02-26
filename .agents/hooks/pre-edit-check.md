# Hook: Quality Guardrail
## Trigger
Antes de que Gemini aplique un cambio en el código (especialmente en entidades o seguridad).

## Acciones
1. Ejecuta el comando de shell `!./mvnw spotless:check` para verificar el formato.
2. Si el cambio es en un Service, exige que Gemini proponga también el test unitario en JUnit 5 correspondiente.
3. Bloquea cualquier cambio que elimine la anotación `@Transactional` en métodos de escritura.