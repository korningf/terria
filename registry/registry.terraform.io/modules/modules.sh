
# -------------------------------------------------
# Hashicorp Terraform
# -------------------------------------------------

mkdir -p hashicorp/



# -------------------------------------------------
# Microsoft Azure
# -------------------------------------------------

mkdir -p azure/



mkdir -p azure/avm-utl-regions/0.1.0/azurerm
curl -o azure/avm-utl-regions/0.1.0/azurerm/terraform-module-avm-utl-regions_azurerm_0.1.0.zip https://github.com/Azure/terraform-azurerm-avm-utl-regions/archive/refs/tags/v0.1.0.zip

mkdir -p azure/avm-utl-interfaces/0.5.1/azurerm
curl -o azure/avm-utl-interfaces/0.5.1/azurerm/terraform-module-avm-utl-interfaces_azurerm_0.5.1.zip https://github.com/Azure/terraform-azurerm-avm-utl-interfaces/archive/refs/tags/v0.5.1.zip

mkdir -p azure/avm-res-network-virtualnetwork/0.9.2/azurerm
curl -o azure/avm-res-network-virtualnetwork/0.9.2/azurerm/terraform-module-avm-res-network-virtualnetwork_azurerm_0.9.2.zip https://github.com/Azure/terraform-azurerm-avm-res-network-virtualnetwork/archive/refs/tags/0.9.2.zip

mkdir -p azure/avm-res-network-privatednszone/0.5.0/azurerm
curl -o azure/avm-res-network-privatednszone/0.5.0/azurerm/terraform-module-avm-res-network-privatednszone_azurerm_0.5.0.zip https://github.com/Azure/terraform-azurerm-avm-res-network-privatednszone/archive/refs/tags/v0.5.0.zip

mkdir -p azure/avm-res-apimanagement-service/0.0.6/azurerm
curl -o azure/avm-res-apimanagement-service/0.0.6/azurerm/terraform-module-avm-res-apimanagement-service_azurerm_0.0.6.zip https://github.com/Azure/terraform-azurerm-avm-res-apimanagement-service/archive/refs/tags/v0.0.6.zip


