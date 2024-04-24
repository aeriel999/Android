using Mapster;
using MapsterMapper;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.OpenApi.Models;
using ShopApp.Entities.Identity;
using ShopApp.Interfaces;
using ShopApp.Persistence;
using ShopApp.Repositories;
using ShopApp.Services;
using System.Reflection;

namespace ShopApp
{
	public static class DependencyInjection
	{
		public static IServiceCollection AddPersistence(this IServiceCollection services)
		{
			services.AddDbContext<ShopAppDbContext>(opt =>
			{
				opt.UseSqlite("Data Source=shop_app_db");
				opt.UseQueryTrackingBehavior(QueryTrackingBehavior.NoTracking);
			});
			services.AddScoped<CategoryService>();
			services.AddTransient<CategoryService>();
			services.AddScoped<CategoryRepositories>();
			services.AddScoped<ImageStorageService>();
			services.AddTransient<ImageStorageService>();
			services.AddAppIdentity();
			services.AddScoped<IJwtTokenService, JwtTokenService>();
			return services;
		}

		public static IServiceCollection AddSwagger(this IServiceCollection services)
		{
			services.AddEndpointsApiExplorer();

			services.AddSwaggerGen(option =>
			{
				option.SwaggerDoc("v1", new OpenApiInfo { Title = "Dashboard API", Version = "v1" });

				//option.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
				//{
				//	In = ParameterLocation.Header,
				//	Description = "Please enter a valid token",
				//	Name = "Authorization",
				//	Type = SecuritySchemeType.Http,
				//	BearerFormat = "JWT",
				//	Scheme = "Bearer"
				//});

				//option.AddSecurityRequirement(new OpenApiSecurityRequirement
				//{
				//	{
				//		new OpenApiSecurityScheme
				//		{
				//			Reference = new OpenApiReference
				//			{
				//				Type=ReferenceType.SecurityScheme,
				//				Id="Bearer"
				//			}
				//		},
				//		Array.Empty<string>()
				//	}
				//});
			});

			services.AddSwaggerGen();

			return services;
		}

		public static IServiceCollection AddMappings(this IServiceCollection services)
		{
			var config = TypeAdapterConfig.GlobalSettings;
			config.Scan(Assembly.GetExecutingAssembly());

			services.AddSingleton(config);
			services.AddScoped<IMapper, ServiceMapper>();

			return services;
		}

		private static IServiceCollection AddAppIdentity(this IServiceCollection services)
		{
			services.AddIdentity<User, Role>(option =>
			{
				option.SignIn.RequireConfirmedEmail = true;
				option.Lockout.MaxFailedAccessAttempts = 5;
				option.Lockout.DefaultLockoutTimeSpan = TimeSpan.FromMinutes(5);
				option.Password.RequireDigit = true;
				option.Password.RequireLowercase = true;
				option.Password.RequireUppercase = true;
				option.Password.RequiredLength = 6;
				option.Password.RequireNonAlphanumeric = true;
				option.User.RequireUniqueEmail = true;
			})
				.AddEntityFrameworkStores<ShopAppDbContext>().AddDefaultTokenProviders();

			return services;
		}
	}
}

