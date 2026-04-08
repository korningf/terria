
# sample repository structure

```text
registry-storage/
|
├── providers/
│   │
│   ├── terraform@ -> registry.terraform.io/                    
│   ├── registry.terraform.io/                              # {registry}
│   │   ├── namespaces.json   
│   │   │
│   │   ├── hashicorp/                                      # {namespace}
│   │   │   ├── providers.json
│   │   │   ├── aws@ -> terraform-provider-aws/         
│   │   │   ├── terraform-provider-aws/                     # {provider_name}
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 6.36.0/
│   │   │   │   │
│   │   │   │   └── 6.36.0/                                 # {provider_version}
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/                             # {provider_platform}
│   │   │   │       │   └── amd64/
│   │   │   │       ├── freebsd/
│   │   │   │       │   └── amd64/
│   │   │   │       ├── linux/
│   │   │   │       │   └── amd64/
│   │   │   │       ├── windows/
│   │   │   │       │   └── amd64/
│   │   │   │       │ 
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.zip
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.sha256
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.sha256.sig
│   │   │   │       │
│   │   │   │       ├── terraform-provider-aws_6.36.0_windows_amd64.zip
│   │   │   │       ├── terraform-provider-aws_6.36.0_windows_amd64.sha256
│   │   │   │       └── terraform-provider-aws_6.36.0_windows_amd64.sha256.sig
│   │   │   │   
│   │   │   ├── azuread@ -> terraform-provider-azuread/
│   │   │   ├── terraform-provider-azuread/
│   │   │   │   ├── latest@ -> 3.8.0/
│   │   │   │   │
│   │   │   │   └── 3.8.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │
│   │   │   ├── azurerm@ -> terraform-provider-azurerrm/
│   │   │   ├── terraform-provider-azurerm/
│   │   │   │   ├── latest@ -> 4.64.0/
│   │   │   │   │
│   │   │   │   └── 4.64.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── google@ -> terraform-provider-google/
│   │   │   ├── terraform-provider-google/
│   │   │   │   ├── latest@ -> 7.24.0/
│   │   │   │   │
│   │   │   │   └── 7.24.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── local@ -> terraform-provider-local/
│   │   │   ├── terraform-provider-local/
│   │   │   │   ├── latest@ -> 2.7.0/
│   │   │   │   │
│   │   │   │   └── 2.7.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── random@ -> terraform-provider-random/
│   │   │   ├── terraform-provider-random/
│   │   │   │   ├── latest@ -> 3.8.1/
│   │   │   │   │
│   │   │   │   └── 3.8.1/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── vault@ -> terraform-provider-vault/
│   │   │   ├── terraform-provider-vault/
│   │   │   │   ├── latest@ -> 5.8.0/
│   │   │   │   │
│   │   │   │   └── 5.8.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │
│   │   │   └── null/
│   │   │
│   │   ├── aws/
│   │   │         
│   │   ├── azure/
│   │   │         
│   │   ├── google
│   │   │         
│   │   └── oracle/       
│   │          
│   └── internal.registry/
│       ├── namespaces.json   
│       └── terria/
│           ├── providers/
│           └── abstract-cloud/
│               ├── versions.json   
│               └── 1.0.0/
│                   ├── platforms.json
│                   ├── windows/
│                   ├── linux/
│                   ├── darwin/
│                   └── freebsd/
│
├── modules/
|   | 
│   ├── terraform@ -> registry.terraform.io/
│   ├── registry.terraform.io/                      # {registry}
│   │   ├── namespaces.json                         
|   |   │
│   │   ├── hashicorp@ -> hashicorp-modules/
│   │   ├── hashicorp-modules/                      # {namespace}
│   │   │   ├── modules.json   
│   │   │   │
│   │   │   └── tfe-workspaces/                     # {module_name}
│   │   │       ├── versions.json   
│   │   │       ├── latest@ -> 0.1.1/  
│   │   │       │
│   │   │       └── 0.1.1/                          # {module_version}    
│   │   │           ├── providers.json 
│   │   │           └── tfe/                        # {module_provider}
│   │   │         
│   │   ├── aws@ -> terraform-aws-modules/
│   │   ├── terraform-aws-modules/
│   │   │   ├── modules.json   
│   │   │   │
│   │   │   ├── identity-access@ -> iam/
│   │   │   ├── iam/
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 6.4.0/
│   │   │   │   │
│   │   │   │   └── 6.4.0/
│   │   │   │       ├── providers.json 
│   │   │   │       └── aws/
│   │   │   │       
│   │   │   ├── static-storage@ -> s3/
│   │   │   └── s3/
│   │   │       ├── versions.json   
│   │   │       ├── latest@ -> 5.10.0/
│   │   │       │
│   │   │       └── 5.10.0/
│   │   │           ├── providers.json 
│   │   │           └── aws/
│   │   │         
│   │   ├── google@ -> terraform-google-modules/
│   │   ├── terraform-google-modules/
│   │   │   ├── modules.json   
│   │   │   │
│   │   │   ├── identity-access@ -> iam/
│   │   │   ├── iam/
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 8.2.0/
│   │   │   │   │
│   │   │   │   └── 8.2.0/
│   │   │   │       ├── providers.json 
│   │   │   │       └── google/
│   │   │   │       
│   │   │   ├── static-storage@ -> cloud-storage/
│   │   │   └── cloud-storage/
│   │   │       ├── versions.json   
│   │   │       ├── latest@ -> 12.3.0/
│   │   │       │
│   │   │       └── 12.3.0/
│   │   │           ├── providers.json 
│   │   │           └── google/
│   │   │       
│   │   ├── azure@ -> terraform-azure-modules/
│   │   ├── terraform-azure-modules/
│   │   │   ├── modules.json   
│   │   │   │    
│   │   │   ├── naming@ -> terraform-azurerm-naming/
│   │   │   ├── terraform-azurerm-naming/
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 0.4.3/
│   │   │   │   │
│   │   │   │   └── 0.4.3/
│   │   │   │       ├── providers.json 
│   │   │   │       └── azurerm/
│   │   │   │
│   │   │   ├── vnet@ -> terraform-azurerm-vnet/
│   │   │   ├── terraform-azurerm-vnet/
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 5.0.1/
│   │   │   │   │
│   │   │   │   └── 5.0.1/
│   │   │   │       ├── providers.json 
│   │   │   │       └── azurerm/
│   │   │   │            
│   │   │   ├── avm-ptn-alz@ -> terraform-azurerm-avm-ptn-alz/    
│   │   │   ├── terraform-azurerm-avm-ptn-alz/
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 0.19.0/
│   │   │   │   │
│   │   │   │   └── 0.19.0/
│   │   │   │       ├── providers.json 
│   │   │   │       └── azurerm/
│   │   │   │            
│   │   │   ├── avm-res-network-virtualnetwork@ -> terraform-azurerm-avm-res-network-virtualnetwork/    
│   │   │   └── terraform-azurerm-avm-res-network-virtualnetwork/
│   │   │       ├── versions.json   
│   │   │       ├── latest@ -> 0.17.1/
│   │   │       │
│   │   │       └── 0.17.1/
│   │   │           ├── providers.json 
│   │   │           └── azurerm/
│   │   │
│   │   └── kubernetes
│   │
│   └── internal.registry/
│       ├── namespaces.json                         
│       │
│       └── terria/
│           ├── modules.json   
│           │
│           └── abstract-cloud-vpc/
│               ├── versions.json 
│               │  
│               └── 1.0.0/
│                   ├── providers.json
│                   ├── aws/
│                   ├── azurerm/
│                   └── google/
│
└── 
```                    





# alternate repository structure

```
registry-storage/
│
├── terraform@ -> registry.terraform.io/                    
├── registry.terraform.io/                                  # {registry}
│   │       
│   ├──providers/
│   │       
│   └──modules/
│       
├── kubernetes@ -> registry.k8s.io/
├── registry.k8s.io/                                      # {registry}
│   │       
│   ├──plugins/
│   │       
│   └──charts/
│       
├── docker@ -> registry.hub.docker.com//                    
└── registry.hub.docker.com/                              # {registry}
    │   
    ├──manifests/
    │       
    └──images/

```


```text
registry-storage/
│
├── terraform@ -> registry.terraform.io/                    
├── registry.terraform.io/                                  # {registry}
│   │       
│   ├──providers/
│   │   ├── namespaces.json   
│   │   │
│   │   ├── hashicorp/                                      # {namespace}
│   │   │   ├── providers.json
│   │   │   │
│   │   │   ├── aws@ -> terraform-provider-aws/         
│   │   │   ├── terraform-provider-aws/                     # {provider_name}
│   │   │   │   ├── versions.json   
│   │   │   │   ├── latest@ -> 6.36.0/
│   │   │   │   │
│   │   │   │   └── 6.36.0/                                 # {provider_version}
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/                             # {provider_platform}
│   │   │   │       │   └── amd64/
│   │   │   │       ├── freebsd/
│   │   │   │       │   └── amd64/
│   │   │   │       ├── linux/
│   │   │   │       │   └── amd64/
│   │   │   │       ├── windows/
│   │   │   │       │   └── amd64/
│   │   │   │       │ 
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.zip
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.sha256
│   │   │   │       ├── terraform-provider-aws_6.36.0_linux_amd64.sha256.sig
│   │   │   │       │
│   │   │   │       ├── terraform-provider-aws_6.36.0_windows_amd64.zip
│   │   │   │       ├── terraform-provider-aws_6.36.0_windows_amd64.sha256
│   │   │   │       └── terraform-provider-aws_6.36.0_windows_amd64.sha256.sig
│   │   │   │   
│   │   │   ├── azuread@ -> terraform-provider-azuread/
│   │   │   ├── terraform-provider-azuread/
│   │   │   │   ├── latest@ -> 3.8.0/
│   │   │   │   │
│   │   │   │   └── 3.8.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │
│   │   │   ├── azurerm@ -> terraform-provider-azurerrm/
│   │   │   ├── terraform-provider-azurerm/
│   │   │   │   ├── latest@ -> 4.64.0/
│   │   │   │   │
│   │   │   │   └── 4.64.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── google@ -> terraform-provider-google/
│   │   │   ├── terraform-provider-google/
│   │   │   │   ├── latest@ -> 7.24.0/
│   │   │   │   │
│   │   │   │   └── 7.24.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── local@ -> terraform-provider-local/
│   │   │   ├── terraform-provider-local/
│   │   │   │   ├── latest@ -> 2.7.0/
│   │   │   │   │
│   │   │   │   └── 2.7.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── random@ -> terraform-provider-random/
│   │   │   ├── terraform-provider-random/
│   │   │   │   ├── latest@ -> 3.8.1/
│   │   │   │   │
│   │   │   │   └── 3.8.1/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │   
│   │   │   ├── vault@ -> terraform-provider-vault/
│   │   │   ├── terraform-provider-vault/
│   │   │   │   ├── latest@ -> 5.8.0/
│   │   │   │   │
│   │   │   │   └── 5.8.0/
│   │   │   │       ├── platforms.json
│   │   │   │       ├── darwin/
│   │   │   │       ├── freebsd/
│   │   │   │       ├── linux/
│   │   │   │       └── windows/
│   │   │   │
│   │   │   └── null/
│   │   │
│   │   ├── aws/
│   │   │         
│   │   ├── azure/
│   │   │         
│   │   ├── google
│   │   │         
│   │   ├── oracle/       
│   │   │
│   │   └── kubernetes
│   │           
│   └── modules/
│       ├── namespaces.json                         
│       │
│       ├── hashicorp@ -> hashicorp-modules/
│       ├── hashicorp-modules/                      # {namespace}
│       │   ├── modules.json   
│       │   │
│       │   └── tfe-workspaces/                     # {module_name}
│       │       ├── versions.json   
│       │       ├── latest@ -> 0.1.1/  
│       │       │
│       │       └── 0.1.1/                          # {module_version}    
│       │           ├── providers.json 
│       │           └── tfe/                        # {module_provider}
│       │         
│       ├── aws@ -> terraform-aws-modules/
│       ├── terraform-aws-modules/
│       │   ├── modules.json   
│       │   │
│       │   ├── identity-access@ -> iam/
│       │   ├── iam/
│       │   │   ├── versions.json   
│       │   │   ├── latest@ -> 6.4.0/
│       │   │   │
│       │   │   └── 6.4.0/
│       │   │       ├── providers.json 
│       │   │       └── aws/
│       │   │       
│       │   ├── static-storage@ -> s3/
│       │   └── s3/
│       │       ├── versions.json   
│       │       ├── latest@ -> 5.10.0/
│       │       │
│       │       └── 5.10.0/
│       │           ├── providers.json 
│       │           └── aws/
│       │         
│       ├── google@ -> terraform-google-modules/
│       ├── terraform-google-modules/
│       │   ├── modules.json   
│       │   │
│       │   ├── identity-access@ -> iam/
│       │   ├── iam/
│       │   │   ├── versions.json   
│       │   │   ├── latest@ -> 8.2.0/
│       │   │   │
│       │   │   └── 8.2.0/
│       │   │       ├── providers.json 
│       │   │       └── google/
│       │   │       
│       │   ├── static-storage@ -> cloud-storage/
│       │   └── cloud-storage/
│       │       ├── versions.json   
│       │       ├── latest@ -> 12.3.0/
│       │       └── 12.3.0/
│       │           ├── providers.json 
│       │           └── google/
│       │       
│       ├── azure@ -> terraform-azure-modules/
│       ├── terraform-azure-modules/
│       │   ├── modules.json   
│       │   │    
│       │   ├── naming@ -> terraform-azurerm-naming/
│       │   ├── terraform-azurerm-naming/
│       │   │   ├── versions.json   
│       │   │   ├── latest@ -> 0.4.3/
│       │   │   │
│       │   │   └── 0.4.3/
│       │   │       ├── providers.json 
│       │   │       └── azurerm/
│       │   │
│       │   ├── vnet@ -> terraform-azurerm-vnet/
│       │   ├── terraform-azurerm-vnet/
│       │   │   ├── versions.json   
│       │   │   ├── latest@ -> 5.0.1/
│       │   │   │
│       │   │   └── 5.0.1/
│       │   │       ├── providers.json 
│       │   │       └── azurerm/
│       │   │            
│       │   ├── avm-ptn-alz@ -> terraform-azurerm-avm-ptn-alz/    
│       │   ├── terraform-azurerm-avm-ptn-alz/
│       │   │   ├── versions.json   
│       │   │   ├── latest@ -> 0.19.0/
│       │   │   │
│       │   │   └── 0.19.0/
│       │   │       ├── providers.json 
│       │   │       └── azurerm/
│       │   │            
│       │   ├── avm-res-network-virtualnetwork@ -> terraform-azurerm-avm-res-network-virtualnetwork/    
│       │   └── terraform-azurerm-avm-res-network-virtualnetwork/
│       │       ├── versions.json   
│       │       ├── latest@ -> 0.17.1/
│       │       │
│       │       └── 0.17.1/
│       │           ├── providers.json 
│       │           └── azurerm/
│       │
│       └── kubernetes
│   
├── localhost@ -> 
├── localhost.internal.registry/
│   │
│   ├── providers/
│   │   ├── namespaces.json   
│   │   │
│   │   └── terria/
│   │       ├── providers.json   
│   │       │
│   │       └── abstract-cloud/
│   │           ├── versions.json   
│   │           │
│   │           └── 1.0.0/
│   │               ├── platforms.json
│   │               ├── windows/
│   │               ├── linux/
│   │               ├── darwin/
│   │               └── freebsd/
│   │         
│   └── modules/
│       ├── namespaces.json
│       │
│       └── terria/
│           ├── modules.json   
│           │
│           └── abstract-cloud-vpc/
│               ├── versions.json 
│               │  
│               └── 1.0.0/
│                   ├── providers.json
│                   ├── aws/
│                   ├── azurerm/
│                   └── google/
│
│
│ 
├── kubernetes@ -> registry.k8s.io/
├── registry.k8s.io/                                      # {registry}
│
├── docker@ -> registry.hub.docker.com//                    
└── registry.hub.docker.com/                              # {registry}

```                    