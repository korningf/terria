
# -------------------------------------------------
# Hashicorp Terraform
# -------------------------------------------------

mkdir -p hashicorp/




# Terraform provider random 3.8.0
#    provides:   hashicorp/random
#    binaries:   https://releases.hashicorp.com/terraform-provider-random/
mkdir -p hashicorp/random/
curl -o  hashicorp/random/terraform-provider-random_3.8.0_windows_amd64.zip  https://releases.hashicorp.com/terraform-provider-random/3.8.0/terraform-provider-random_3.8.0_windows_amd64.zip




# -------------------------------------------------
# Microsoft Azure
# -------------------------------------------------

mkdir -p azure/

mkdir -p hashicorp/azure/
curl -o hashicorp/azure/terraform-provider-azure_0.1.1_windows_amd64.zip https://releases.hashicorp.com/terraform-provider-azure/0.1.1/terraform-provider-azure_0.1.1_windows_amd64.zip

mkdir -p hashicorp/azuread/
curl -o hashicorp/azuread/terraform-provider-azuread_3.8.0_windows_amd64.zip https://releases.hashicorp.com/terraform-provider-azuread/3.8.0/terraform-provider-azuread_3.8.0_windows_amd64.zip

mkdir -p hashicorp/azurerm/
curl -o hashicorp/azurerm/terraform-provider-azurerm_4.67.0_windows_amd64.zip https://releases.hashicorp.com/terraform-provider-azurerm/4.67.0/terraform-provider-azurerm_4.67.0_windows_amd64.zip

mkdir -p hashicorp/azurestack/
curl -o hashicorp/azurestack/terraform-provider-azurestack_1.0.0_windows_amd64.zip https://releases.hashicorp.com/terraform-provider-azurestack/1.0.0/terraform-provider-azurestack_1.0.0_windows_amd64.zip

mkdir -p hashicorp/azuredevops/
curl -o hashicorp/azuredevops/terraform-provider-azuredevops_1.15.0_windows_amd64.zip https://releases.hashicorp.com/terraform-provider-azuredevops/1.15.0/terraform-provider-azuredevops_1.15.0_windows_amd64.zip