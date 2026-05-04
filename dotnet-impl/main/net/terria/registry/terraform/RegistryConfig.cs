using System.IO;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;

namespace net.terria.registry.terraform
{
    public static class RegistryConfig
    {
        private const string DefaultStoragePath = "../../registry";

        public static void AddRegistryServices(this WebApplicationBuilder builder)
        {
            builder.Services.AddSingleton<RegistryService>();
            builder.Services.AddControllers();
        }

        public static void UseRegistryStaticFiles(this WebApplication app)
        {
            var storagePath = app.Configuration.GetValue<string>("registry.storage.path")
                ?? app.Configuration.GetValue<string>("Registry:StoragePath")
                ?? DefaultStoragePath;
            
            // If the path is relative, resolve it relative to the application content root
            if (!Path.IsPathRooted(storagePath))
            {
                storagePath = Path.GetFullPath(storagePath, app.Environment.ContentRootPath);
            }
            
            var providersPath = Path.Combine(storagePath, "registry.terraform.io", "providers");
            var modulesPath = Path.Combine(storagePath, "registry.terraform.io", "modules");

            if (Directory.Exists(providersPath))
            {
                app.UseStaticFiles(new StaticFileOptions
                {
                    FileProvider = new PhysicalFileProvider(providersPath),
                    RequestPath = "/storage/providers",
                    ServeUnknownFileTypes = true
                });
            }

            if (Directory.Exists(modulesPath))
            {
                app.UseStaticFiles(new StaticFileOptions
                {
                    FileProvider = new PhysicalFileProvider(modulesPath),
                    RequestPath = "/storage/modules",
                    ServeUnknownFileTypes = true
                });
            }
        }
    }
}
