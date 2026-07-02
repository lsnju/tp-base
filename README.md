# tp-base

## current version <version>3.1.31</version>

## docs

- [入门说明](doc/入门说明.md)
- [详细文档](doc/详细文档.md)

## usage

```xml

<dependencyManagement>
    <dependencies>
        <!-- https://mvnrepository.com/artifact/com.lsnju.tp3/tp-bom -->
        <dependency>
            <groupId>com.lsnju.tp3</groupId>
            <artifactId>tp-bom</artifactId>
            <version>3.1.31</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

```xml

<dependencies>
    <!-- https://mvnrepository.com/artifact/com.lsnju.tp3/tp-base -->
    <dependency>
        <groupId>com.lsnju.tp3</groupId>
        <artifactId>tp-base</artifactId>
    </dependency>
</dependencies>
```
