# terria - generic local registry

Terria is a minimal generic local registry for cloud-ops and dev-ops IaC.

It is a proxy mirror meant to cache repos behind an institutional firewall.

For now it holds a terraform registry (we could add docker and kubernetes).

It is implemented in java and asp dotnet, and relies on native symlinks.


# installation


# configuration


# preparation

we want to use a local proxy or local mirrors for common repositories.

the registry itself will require repos maven for java or nuget for donet.

registry contents will cache a copy of local provider plugins and modules.


## repositories

### terraform

```
https://registry.terraform.io/
https://releases.hashicorp.com/
https://azure.github.io/
https://github.com/azure/
https://github.com/hashicorp/ 
```


### java / maven

```
https://repo1.maven.org/
https://maven.oracle.com/
https://mvnrepository.com/
```

### dotnet / nuget

```
TODO
```
