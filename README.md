# Config Properties
 
## Description

Simplifie la configuration à partir d'un fichier interne ou externe

## Configuration

Ajouter dans le pom.xml :

```xml
<dependency>
	<groupId>com.synaptix</groupId>
	<artifactId>config-properties</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Utilisation

Créer un objet component de configuration

``` java
ComponentBean
public interface IConfig extends IComponent {

    String getNomadeSerlvetUrl();

    ServiceImplType getServiceImplType();

    long getMaxSizeUploadAvarieImage();

    Path getPublicAttachmentsDirectory();

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }
}
```

Créer la config :

``` java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
        ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake));
builder.configProperty(ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));
builder.configProperty(ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")));
IConfig config = builder.build();
```

Par défault, il lit dans la variables système `config.file` pour trouver le chemin du fichier ou en interne dans `config.properties` à partir de la racine.

Les valeurs peuvent être changé :
 
``` java
builder.configLoader(DefaultConfigLoader.newBuilder().systemPropertyName("configuration").internalPropertiesPath("others/others.properties").build());
```
