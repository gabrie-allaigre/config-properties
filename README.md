# Config Properties
 
## Description

Permet la transformation de IComponent en json pour Jackson

## Configuration

Ajouter dans le pom.xml :

```xml
<dependency>
	<groupId>com.synaptix</groupId>
	<artifactId>component-jackson</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Utilisation

Si pas de définition de nouveau module pour `ObjectMapper`, ajouter à Jackson `ComponentBeanJacksonConfig`.

Sinon ajouter le module à votre `ObjectMapper`

``` java
objectMapper = new ObjectMapper();
objectMapper.registerModule(new ComponentBeanModule());
```

Si vous avez des champs dans le Json qui ne sont pas dans votre Component ajouter

``` java
this.objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
```
