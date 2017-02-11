# Config Properties
 
## Description

Simplifie la configuration à partir d'un fichier interne ou externe de `properties` vers un Component Bean.

## Configuration

Ajouter dans le pom.xml :

```xml
<dependency>
	<groupId>com.talanlabs</groupId>
	<artifactId>config-properties</artifactId>
	<version>1.0.2</version>
</dependency>
```

## Utilisation

### Automatique

Créer un objet component de configuration automatiquement.

```java
@ComponentBean
public interface IConfig extends IComponent {

    String getName();

    Type getType();

    long getSize();

    @DefaultPropertyValue("51")
    int getWidth();

    @PropertyKey("toto")
    int getTiti();

    ISubConfig getSubConfig();

    enum Type {
        NomadeServlet, Fake, RusService
    }

    @ComponentBean
    interface ISubConfig extends IComponent {

        String getSmtpHost();

        Integer getSmtpPort();

    }   
}
```

Créer la config :

```java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(AutoConfigProperty.newBuilder().build());
IConfig config = builder.build();
```

Le fichier properties correspondant :

```properties
name=Toto
type=NomadeServlet
size=10
toto=15
subConfig.smtpHost=rien.com
subConfig.smtpPort=22
```

Différentes options disponible dans le builder

|Methode|Description|
|------|-----------|
| prefix | Permet de définir un préfix dans les properties           |
| rtextConfiguration | Permet de définir une configuration pour rtext          |

### Manuel

Créer un objet component de configuration.

```java
@ComponentBean
public interface IConfig extends IComponent {

    String getNomadeSerlvetUrl();

    ServiceImplType getServiceImplType();

    long getMaxSizeUploadAvarieImage();

    Path getPublicAttachmentsDirectory();

    IMailConfig getMailConfig();

    List<String> getGroups();

    String[] getRoles();

    Set<String> getEnums();

    Map<ServiceImplType, Boolean> getBooleanMap();

    enum ServiceImplType {
        NomadeServlet, Fake, RusService
    }
    
    @ComponentBean
    public interface IMailConfig extends IComponent {
    
        String getSmptHost();
    
        Integer getSmptPort();
    
    }
}
```

Créer la config :

```java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(ConfigProperty.toString("server.nomade-servlet.url", ConfigFields.nomadeSerlvetUrl, null),
        ConfigProperty.toGeneric("server.service-impl.type", ConfigFields.serviceImplType, IConfig.ServiceImplType::valueOf, IConfig.ServiceImplType.Fake));
builder.configProperty(ConfigProperty.toLong("server.max-image-upload-avarie", ConfigFields.maxSizeUploadAvarieImage, 1024L * 1024L /* 1Mo */));
builder.configProperty(ConfigProperty.toGeneric("server.public-attachments-path", ConfigFields.publicAttachmentsDirectory, Paths::get, Paths.get("public/attachments/")));
builder.configProperty(ConfigProperty.toString("server.mail.smtp.host", ConfigFields.mailConfig().dot().smptHost().name(), null),
        ConfigProperty.toInteger("server.mail.smtp.port", ConfigFields.mailConfig().dot().smptPort().name(), null));
builder.configProperty(CollectionConfigProperty.toList("server.groups", ConfigFields.groups, ConfigProperty.STRING_FROM_STRING, null),
        CollectionConfigProperty.toSet("server.enums", ConfigFields.enums, ConfigProperty.STRING_FROM_STRING, null));
builder.configProperty(ArrayConfigProperty.toArrayString("server.roles", ConfigFields.roles,  null));
builder.configProperty(MapConfigProperty.toHashMap("server.booleans", ConfigFields.booleanMap, IConfig.ServiceImplType::valueOf, ConfigProperty.BOOLEAN_FROM_STRING, null));
IConfig config = builder.build();
```

Par défault, il lit dans la variables système `config.file` pour trouver le chemin du fichier ou en interne dans `config.properties` à partir de la racine.

Le loader peut être modifier, pour changer le default :
 
``` java
builder.configLoader(DefaultConfigLoader.newBuilder().systemPropertyName("configuration").internalPropertiesPath("others/others.properties").build());
```

Différents `Config Loader` :

``` java
IConfigLoader.system("property"); // Nom de la variable système
IConfigLoader.url("http://toto.com/config.properties"); // URL vers la config 
IConfigLoader.resource("config.properties"); // Chemin interne
IConfigLoader.file("C:/config.properties"); // Chemin système
```

Possibiliter de les cumuler :

``` java
IConfigLoader.compose(IConfigLoader.system("property"),IConfigLoader.resource("config.properties")); // Additione tous les propreties trouvées
IConfigLoader.firstNotNull(IConfigLoader.system("property"),IConfigLoader.resource("config.properties")); // Le premier non null

```

Le fichier `.properties` contient la liste des clefs valeurs.

``` properties
# Exemple Simple
server.number=10
server.mail.smtp.host=smpt.gmail.com

# Exemple List, par défaut séparateur ","
server.groups=admin,user

# Exemple de Map, la clef est le text après server.booleans.
server.booleans.NomadeServlet=true
server.booleans.Fake=false
```

## Détails des types définits

| Type | IConfigProperty |
|---|------------|
| String | ConfigProperty.toString |
| Integer | ConfigProperty.toInteger |
| Long | ConfigProperty.toLong |
| Double | ConfigProperty.toDouble |
| Float | ConfigProperty.toFloat |
| Enum, ... | ConfigProperty.toGeneric |
| List<E> | CollectionConfigProperty.toList |
| Set<E> | CollectionConfigProperty.toSet |
| String[] | ArrayConfigProperty.toArrayString |
| Integer[] | ArrayConfigProperty.toArrayInteger |
| Long[] | ArrayConfigProperty.toArrayLong |
| Double[] | ArrayConfigProperty.toArrayDouble |
| Float[] | ArrayConfigProperty.toArrayFloat |
| Boolean[] | ArrayConfigProperty.toArrayBoolean |
| E[] | ArrayConfigProperty.toArrayGeneric |
| Map<E,F> | MapConfigProperty.toHashMap |
| Properties | PropertiesConfigProperty |

**Vous pouvez ajouter d'autres types en créant une class implementant un `IConfigProperty`**