# Diseño: alertas de vencimiento documental

Fecha: 2026-06-19

## Objetivo

Incorporar alertas internas persistentes para avisar al operador asignado a un documento y a todos los administradores cuando su fecha de vencimiento esté a 30, 7 o 1 día, o haya llegado. La solución debe conservar las alertas como historial y permitir añadir correo y Telegram posteriormente sin acoplar esos canales al scheduler.

## Alcance inicial

- Agregar una fecha de vencimiento opcional a `Documento`.
- Crear alertas internas persistentes y consultables por el usuario autenticado.
- Ejecutar diariamente la evaluación de vencimientos.
- Permitir marcar una alerta o todas las alertas propias como leídas.
- Preparar una interfaz de notificación extensible.
- No implementar todavía correo ni Telegram.
- No implementar eliminación de alertas.

## Modelo de datos

### Documento

Se agrega `fechaVencimiento: LocalDate`. El campo será opcional porque no todos los documentos, especialmente los de tipo `OTRO`, necesariamente vencen. Los documentos sin fecha serán ignorados por la evaluación automática.

La fecha se incluirá en `DocumentoDTO` y en la carga de archivos como parámetro multipart opcional.

### Alerta

La entidad `Alerta` contiene:

- `id: Long`
- `titulo: String`
- `mensaje: String`
- `fechaCreacion: LocalDateTime`
- `leida: boolean`
- `tipo: TipoAlerta`
- `usuario: Usuario`
- `documento: Documento`

`TipoAlerta` admite exactamente:

- `VENCE_30_DIAS`
- `VENCE_7_DIAS`
- `VENCE_1_DIA`
- `VENCIDA`

Una restricción única sobre `(usuario_id, documento_id, tipo)` garantiza idempotencia. Cada destinatario recibe una entidad independiente para que el estado de lectura sea personal.

Las alertas se conservan después de ser leídas. La primera versión no ofrece eliminación ni archivado.

## Arquitectura

Se usará un monolito modular con responsabilidades separadas:

```text
VencimientoScheduler
  -> VencimientoService
    -> AlertaService
      -> NotificationService
        -> SystemNotificationService
```

### Responsabilidades

- `VencimientoScheduler`: dispara la evaluación diaria y no contiene reglas de negocio ni envía mensajes directamente.
- `VencimientoService`: consulta documentos candidatos, calcula el umbral aplicable y solicita la creación de alertas.
- `AlertaService`: resuelve destinatarios y coordina las notificaciones para cada uno.
- `NotificationService`: contrato `notificar(NotificationContext contexto)` para los canales.
- `SystemNotificationService`: implementación inicial que persiste la alerta interna de manera idempotente.
- `EmailNotificationService` y `TelegramNotificationService`: implementaciones futuras del mismo contrato.

`NotificationContext` transporta el destinatario, documento, tipo, título y mensaje sin exponer detalles de persistencia al scheduler.

Cuando existan varios canales, un despachador los ejecutará de forma aislada. Una falla de email o Telegram se registrará y no revertirá la alerta interna ni bloqueará otros canales.

## Destinatarios

Por cada documento en un umbral se notificará a:

1. El usuario vinculado al operador asignado al documento, cuando exista.
2. Todos los usuarios con rol `ADMIN`.

Los destinatarios se deduplicarán antes de notificar. Si el documento no tiene operador, o el operador no tiene usuario, los administradores seguirán recibiendo la alerta.

## Reglas de vencimiento

El cálculo usa fechas de calendario en `America/Mexico_City`:

- diferencia de 30 días: `VENCE_30_DIAS`;
- diferencia de 7 días: `VENCE_7_DIAS`;
- diferencia de 1 día: `VENCE_1_DIA`;
- diferencia menor o igual a 0: `VENCIDA`.

Una fecha vencida produce una sola alerta crítica por destinatario y documento, no una alerta nueva cada día.

Los documentos cuya diferencia sea distinta de esos umbrales no producen alerta. La consulta podrá limitarse a documentos con `fechaVencimiento <= hoy + 30 días` y fecha no nula.

## Programación

Spring Scheduling se habilitará en la aplicación. El scheduler se ejecutará diariamente a las 08:00 con la zona `America/Mexico_City`.

El cron y la zona se externalizarán en `application.properties`, con estos valores predeterminados:

```properties
app.alertas.cron=0 0 8 * * *
app.alertas.zone=America/Mexico_City
```

Se inyectará un `Clock` configurado con la misma zona para mantener el cálculo determinista y comprobable.

## API REST

Todas las operaciones requieren JWT y actúan sobre el usuario autenticado. Ningún endpoint acepta un `usuarioId` elegido por el cliente.

### Consultar historial

`GET /api/v1/alertas`

Parámetro opcional `soloNoLeidas`, con valor predeterminado `false`. Devuelve las alertas propias ordenadas por `fechaCreacion` descendente.

### Contar pendientes

`GET /api/v1/alertas/no-leidas/count`

Devuelve un DTO estable, por ejemplo `{ "count": 3 }`.

### Marcar una alerta

`PATCH /api/v1/alertas/{id}/leer`

Solo permite modificar una alerta perteneciente al usuario autenticado. Una alerta ya leída conserva el mismo resultado sin error.

### Marcar todas

`PATCH /api/v1/alertas/leer-todas`

Marca todas las alertas propias como leídas y devuelve `{ "actualizadas": n }`.

No se incorpora un endpoint de eliminación.

## Respuesta de alerta

`AlertaDTO` contiene información suficiente para React sin serializar entidades JPA:

- `id`
- `titulo`
- `mensaje`
- `fechaCreacion`
- `leida`
- `tipo`
- `documentoId`
- `tipoDocumento`
- `nombreArchivo`
- `fechaVencimiento`

No expone datos sensibles del usuario ni rutas físicas del archivo.

## Seguridad

- `ADMIN` y `OPERADOR` pueden acceder a los endpoints de alertas.
- Las consultas se filtran por el identificador del principal autenticado.
- Marcar como leída valida simultáneamente el identificador de alerta y su propietario.
- Un administrador consulta únicamente sus propias copias de las alertas globales, no las alertas personales de otros usuarios.

## Transacciones y manejo de errores

- La creación de cada alerta interna se realiza transaccionalmente.
- La restricción única es la última defensa contra ejecuciones concurrentes o repetidas.
- Un conflicto por duplicado se considera idempotencia, no un fallo del job.
- La falla al procesar un documento se registra con su identificador y no detiene los demás documentos.
- Los canales externos futuros se ejecutarán después de asegurar la persistencia interna y sus fallos no harán rollback de esta.

## Pruebas

### Unitarias

- Cálculo de los umbrales 30, 7, 1 y vencido mediante un `Clock` fijo.
- Ausencia de alerta fuera de umbral o sin fecha de vencimiento.
- Construcción correcta del título y mensaje.
- Resolución de operador y administradores, incluida la deduplicación.

### Integración

- Creación de una alerta por destinatario.
- Idempotencia al ejecutar la evaluación dos veces.
- Historial ordenado y filtro de no leídas.
- Contador de pendientes.
- Marcado individual y masivo.
- Rechazo al intentar modificar una alerta ajena.
- Conservación de la alerta interna cuando un canal adicional falla.

## Criterios de aceptación

- Un documento puede almacenar y devolver su fecha de vencimiento.
- La ejecución diaria genera alertas únicamente en los cuatro umbrales definidos.
- El operador asignado y todos los administradores reciben alertas independientes.
- Ninguna combinación de destinatario, documento y tipo se duplica.
- Cada usuario ve y marca únicamente sus propias alertas.
- Las alertas leídas permanecen en el historial.
- El horario predeterminado es 08:00 en `America/Mexico_City` y puede configurarse.
- El diseño permite añadir email y Telegram sin modificar el scheduler ni las reglas de vencimiento.
