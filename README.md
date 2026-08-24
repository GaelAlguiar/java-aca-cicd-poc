# ACA CI/CD Demo

Prueba de concepto para validar el despliegue de una aplicación Java desde GitHub Actions hacia Azure Container Apps usando únicamente dependencias públicas.

## Flujo

1. Los pull requests y pushes ejecutan compilación y pruebas con Java 21.
2. El workflow manual de desarrollo corre en un runner local con la etiqueta `aca-poc`.
3. El runner construye una imagen etiquetada con el SHA completo del commit.
4. La imagen se publica en Azure Container Registry.
5. Azure Container Apps crea una revisión con la nueva imagen y las variables de demostración.
6. El workflow consulta `/actuator/health` para verificar el despliegue.

## Requisitos locales

- Java 21.
- Docker Desktop.
- Acceso público a Maven Central.

Maven no necesita instalarse porque el repositorio incluye Maven Wrapper 3.9.11.

```bash
./mvnw clean verify
```

## Ejecutar la aplicación

```bash
APP_SERVICE_NAME=aca-cicd-demo \
APP_ENVIRONMENT=local \
APP_MESSAGE="Prueba local" \
./mvnw spring-boot:run
```

Consultar:

```bash
curl http://localhost:8080/api/info
curl http://localhost:8080/actuator/health
```

## Ejecutar el contenedor

```bash
docker build --tag aca-cicd-demo:local .
docker run --rm --publish 8080:8080 \
  --env APP_SERVICE_NAME=aca-cicd-demo \
  --env APP_ENVIRONMENT=local-docker \
  --env APP_MESSAGE="Prueba desde Docker" \
  aca-cicd-demo:local
```

## Configuración de GitHub

Crear un environment llamado `development` y definir estas variables:

| Variable | Uso |
| --- | --- |
| `AZURE_CLIENT_ID` | Client ID de la identidad federada usada por GitHub Actions. |
| `AZURE_TENANT_ID` | Tenant de Microsoft Entra ID. |
| `AZURE_SUBSCRIPTION_ID` | Suscripción donde vive la PoC. |
| `AZURE_CONTAINER_REGISTRY` | Nombre de ACR sin `.azurecr.io`. |
| `AZURE_RESOURCE_GROUP` | Resource group de la Container App. |
| `AZURE_CONTAINER_APP_NAME` | Nombre de la Azure Container App. |
| `APP_SERVICE_NAME` | Nombre mostrado por `/api/info`. |
| `APP_ENVIRONMENT` | Ambiente mostrado por `/api/info`, por ejemplo `development`. |
| `APP_MESSAGE` | Mensaje de demostración. |

No se necesita `AZURE_CLIENT_SECRET`: el workflow usa OIDC y tokens temporales.

La identidad de despliegue necesita permisos mínimos para publicar en ACR y actualizar la Container App. La Container App debe tener acceso para descargar imágenes del registro.

## Runner local

En el repositorio de GitHub:

1. Abrir **Settings > Actions > Runners**.
2. Seleccionar **New self-hosted runner**.
3. Ejecutar en la máquina local los comandos de descarga y configuración que muestra GitHub.
4. Agregar la etiqueta personalizada `aca-poc` durante la configuración.
5. Instalar o habilitar Docker y Azure CLI para el usuario del runner. La CLI debe incluir el grupo de comandos `az containerapp`.
6. Mantener el proceso del runner activo al ejecutar `Deploy Dev to Azure Container Apps`.

El workflow de despliegue usa las etiquetas `self-hosted` y `aca-poc`, por lo que no puede ejecutarse en un runner hospedado estándar.

## Endpoints

### `GET /api/info`

Respuesta de ejemplo:

```json
{
  "service": "aca-cicd-demo",
  "environment": "development",
  "message": "CI/CD funcionando"
}
```

### `GET /actuator/health`

```json
{
  "status": "UP"
}
```

## Alcance

Esta PoC usa variables de entorno para demostrar un flujo completo de CI/CD. En aplicaciones reales, las propiedades pueden almacenarse en Azure App Configuration y los secretos en Azure Key Vault mediante Managed Identity.
