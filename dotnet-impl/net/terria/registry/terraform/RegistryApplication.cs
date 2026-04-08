using System;
using System.Linq;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Serilog;

namespace net.terria.registry.terraform
{
    /// <summary>
    /// Application entrypoint and HTTP routing for Terraform registry API.
    /// </summary>
    public class RegistryApplication
    {
        /// <summary>
        /// Starts the ASP.NET Core application, configures logging and API route handlers.
        /// </summary>
        /// <param name="args">Startup arguments (not used).</param>
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Configure Serilog as slf4net equivalent.
            builder.Host.UseSerilog((context, config) =>
            {
                config.ReadFrom.Configuration(context.Configuration);
            });

            builder.Services.AddSingleton<RegistryService>();

            var app = builder.Build();
            app.UseSerilogRequestLogging();

            app.MapGet("/providers/{registry}/{namespace}/{name}/versions", (RegistryService service, string registry, string @namespace, string name) =>
            {
                var index = service.GetProvider(registry, @namespace, name);
                if (index == null) return Results.NotFound();

                var versions = index.Versions
                    .OrderByDescending(v => v.Key)
                    .Select(v => new
                    {
                        version = v.Key.ToString(),
                        platforms = v.Value.Select(p => new { os = p.Os, arch = p.Arch })
                    });

                return Results.Ok(new { versions });
            });

            app.MapGet("/providers/{registry}/{namespace}/{name}/{version}/download/{os}/{arch}", (string registry, string @namespace, string name, string version, string os, string arch) =>
            {
                var url = $"/storage/providers/{registry}/{@namespace}/{name}/{version}/{os}/{arch}/terraform-provider-{name}_{version}_{os}_{arch}.zip";
                return Results.Ok(new { download_url = url });
            });

            app.MapGet("/modules/{registry}/{namespace}/{name}/versions", (RegistryService service, string registry, string @namespace, string name) =>
            {
                var module = service.GetModule(registry, @namespace, name);
                if (module == null) return Results.NotFound();

                var versions = module.Versions
                    .OrderByDescending(v => v.Key)
                    .Select(v => new
                    {
                        version = v.Key.ToString(),
                        platforms = v.Value.Platforms.Select(p => new { os = p.Os, arch = p.Arch })
                    });

                return Results.Ok(new { versions });
            });

            app.Run();
        }
    }
}
