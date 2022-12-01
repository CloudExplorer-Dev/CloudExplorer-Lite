OpenStack4j Connectors
======================

By default OpenStack4j uses Jersey 2. You can break this out to allow usage of any of the following connectors Jersey 2,
Resteasy, Apache HttpClient or OKHttp.

### Usage

Instead of depending on the normal "openstack4j" artifactId, change the dependency to be "openstack4j-core" like below:

```xml
<dependency>
    <groupId>com.github.openstack4j.core</groupId>
    <artifactId>openstack4j-core</artifactId>
    <version>...</version>
</dependency>
```

Now choose a connector by adding the applicable dependency below:

**Jersey 2**

```xml
<dependency>
    <groupId>com.github.openstack4j.core.connectors</groupId>
    <artifactId>openstack4j-jersey2</artifactId>
    <version>...</version>
</dependency>
```

**Resteasy**

```xml
<dependency>
    <groupId>com.github.openstack4j.core.connectors</groupId>
    <artifactId>openstack4j-resteasy</artifactId>
    <version>...</version>
</dependency>
```

**Apache HttpClient**

```xml
<dependency>
    <groupId>com.github.openstack4j.core.connectors</groupId>
    <artifactId>openstack4j-httpclient</artifactId>
    <version>...</version>
</dependency>
```

**OKHttp**

```xml
<dependency>
    <groupId>com.github.openstack4j.core.connectors</groupId>
    <artifactId>openstack4j-okhttp</artifactId>
    <version>...</version>
</dependency>
```
