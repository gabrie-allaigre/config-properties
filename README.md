# Config Properties
 
## Description

Simplify configuration with file internal or external `properties` or `yaml` to a Bean.

## Configuration

Add in pom.xml :

```xml
<dependency>
	<groupId>com.talanlabs</groupId>
	<artifactId>config-properties</artifactId>
	<version>2.0.0</version>
</dependency>
```

With a Component Bean (https://github.com/gabrie-allaigre/component-bean)

```xml
<dependency>
	<groupId>com.talanlabs</groupId>
	<artifactId>component-config-properties</artifactId>
	<version>2.0.0</version>
</dependency>
```

## Use

### Auto

Bean:

```java
public class ConfigBean  {

    private String name;
    private Type type;
    private long size;
    @DefaultPropertyValue("51")
    private int width;
    @PropertyKey("toto")
    private int titi;
    private SubConfigBean subConfig;
    private String[] roles;
    private Map<String, String> props;

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public int getWidth() {
        return width;
    }

    public int getTiti() {
        return titi;
    }

    public SubConfigBean getSubConfig() {
        return subConfig;
    }

    public String[] getRoles() {
        return roles;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public enum Type {
        NomadeServlet, Fake, RusService
    }

    @SubConfig
    public static class SubConfigBean {

        @PropertyKey(alternative = "HOST")
        private String smtpHost;
        private Integer smtpPort;
        
        public String getSmtpHost() {
            return smtpHost;
        }

        public Integer getSmtpPort() {
            return smtpPort;
        }
    }
}
```

Create a `config.yml` in resources

```yaml
name: Toto
type: NomadeServlet
size: 10
toto: 15
subConfig:
  smtpHost: rien.com
  smtpPort: 22
roles:
  - admin
  - simple
props:
  sqlVersion: 2
  class: SQL.class
```

or `config.properties`

```properties
name=Toto
type=NomadeServlet
size=10
toto=15
subConfig.smtpHost=rien.com
subConfig.smtpPort=22
roles.1=admin
roles.2=simple
props.sqlVersion=2
props.class=SQL.class
```

Create config builder :

```java
ConfigBuilder<ConfigBean> builder = ConfigBuilder.newBuilder(ConfigBean.class);
builder.configProperty(AutoConfigProperty.newBuilder().build());
ConfigBean config = builder.build();
```

> By default, find first not null file in resources `config.properties` or `config.yml` or get file in environnement variable `config.file`, and compose with system properties and environnement variables.

Options in `ConfigBuilder.newBuilder`

|Method|Description|
|------|-----------|
| configLoader | Configure loader |
| configProperty | Add a config property |

Options in `AutoConfigProperty`

|Method|Description|
|------|-----------|
| prefix | Add prefix all prroperties           |
| rtextConfiguration | Engine configuration to convert `String` to `Object`. https://github.com/gabrie-allaigre/rtext |

#### Annotation

- `@DefaultPropertyValue` set default value if not found

- `@PropertyKey`

|Method|Description|
|------|-----------|
| value | Change name of property |
| alternative | Add other full name, use for environnement variable. Ex: `subConfig.smtpHost` or `SMTP_HOST` |
| ignore | AutoConfig ignore field |
| replaceAll | Only for List or Map, replace or not all value if exist alternative |

- `@SubConfig` set a sub config

### Manuel

Create config builder:

```java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(ConfigProperty.toString("name", ConfigFields.name, null),
        ConfigProperty.toGeneric("type", ConfigFields.type, IConfig.Type::valueOf, IConfig.Type.Fake));
builder.configProperty(ConfigProperty.toLong("size", ConfigFields.size, 0));
builder.configProperty(ConfigProperty.toInt("width", ConfigFields.size, 51));
builder.configProperty(ConfigProperty.toInt("toto", ConfigFields.titi,0);
builder.configProperty(ConfigProperty.toString("subConfig.smtpHost", ConfigFields.subConfig().dot().smptHost().name(), null),
        ConfigProperty.toInteger("subConfig.smtpPort", ConfigFields.subConfig().dot().smptPort().name(), null));
builder.configProperty(CollectionMultiLinesConfigProperty.toList("roles", ConfigFields.roles, ConfigProperty.STRING_FROM_STRING, null));
builder.configProperty(MapConfigProperty.toHashMap("props", ConfigFields.booleanMap, ConfigProperty.STRING_FROM_STRING, ConfigProperty.STRING_FROM_STRING, null));
IConfig config = builder.build();
```

### Loader

Default loader is:

```java
ConfigBuilder<IConfig> builder = ConfigBuilder.newBuilder(IConfig.class);
builder.configLoader(
    IConfigLoader.compose(
        IConfigLoader.firstNotNull(
            IConfigLoader.system("config.file"),
            IConfigLoader.resource("config.properties"),
            IConfigLoader.resource("config.yml")
        ),
        IConfigLoader.systemProperties(),
        IConfigLoader.envProperties())
    );
```

|Method|Description|
|------|-----------|
| IConfigLoader.properties(${properties}) | With `Properties` |
| IConfigLoader.resource(${path}) | Load file in resource, null if not exist |
| IConfigLoader.system(${property}) | Get a property in system properties, and get file if exist |
| IConfigLoader.systemProperties() | Load file in resource, null if not exist |
| IConfigLoader.envProperties() | Load file in resource, null if not exist |
| IConfigLoader.file(${file}) | Load file |
| IConfigLoader.url(${url}) | Load url |
| IConfigLoader.compose(${loaders}) | Merge all loaderr |
| IConfigLoader.firstNotNull(${loaders}) | Get a first loader to get not null properties |

## Details of the defined types

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
| Collection<E> | CollectionConfigProperty.toGeneric |
| List<E> | CollectionMultiLinesConfigProperty.toList |
| Set<E> | CollectionMultiLinesConfigProperty.toSet |
| Collection<E> | CollectionMultiLinesConfigProperty.toGeneric |
| String[] | ArrayConfigProperty.toArrayString |
| Integer[] | ArrayConfigProperty.toArrayInteger |
| Long[] | ArrayConfigProperty.toArrayLong |
| Double[] | ArrayConfigProperty.toArrayDouble |
| Float[] | ArrayConfigProperty.toArrayFloat |
| Boolean[] | ArrayConfigProperty.toArrayBoolean |
| E[] | ArrayConfigProperty.toArrayGeneric |
| String[] | ArrayMultiLinesConfigProperty.toArrayString |
| Integer[] | ArrayMultiLinesConfigProperty.toArrayInteger |
| Long[] | ArrayMultiLinesConfigProperty.toArrayLong |
| Double[] | ArrayMultiLinesConfigProperty.toArrayDouble |
| Float[] | ArrayMultiLinesConfigProperty.toArrayFloat |
| Boolean[] | ArrayMultiLinesConfigProperty.toArrayBoolean |
| E[] | ArrayMultiLinesConfigProperty.toArrayGeneric |
| Map<E,F> | MapConfigProperty.toHashMap |
| Properties | PropertiesConfigProperty |

**You can add other types by creating a class implementing an `IConfigProperty`**

## ComponentBean

Set config with ComponentBean:

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

    List<String> getRoles();
    
    Map<String,String> getProps();

    enum Type {
        NomadeServlet, Fake, RusService
    }

    @SubConfig
    interface ISubConfig extends IComponent {

        @PropertyKey(alternative="SMTP_HOST")
        String getSmtpHost();

        @PropertyKey(alternative="SMTP_PORT")
        Integer getSmtpPort();

    }   
}
```

Create config builder :

```java
ComponentConfigBuilder<IConfig> builder = ComponentConfigBuilder.newBuilder(IConfig.class);
builder.configProperty(AutoConfigProperty.newBuilder().build());
IConfig config = builder.build();
```
