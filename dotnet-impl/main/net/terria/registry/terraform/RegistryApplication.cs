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

            builder.AddRegistryServices();

            var app = builder.Build();
            app.UseSerilogRequestLogging();
            app.UseRegistryStaticFiles();

            app.MapControllers();

            app.Run();
        }
    }
}
