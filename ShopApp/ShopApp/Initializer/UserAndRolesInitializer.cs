using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using ShopApp.Entities.Identity;
using ShopApp.Persistence;

namespace ShopApp.Initializer;

public static class UserAndRolesInitializer
{
	public async static void SeedData(this IApplicationBuilder app)
	{
		using var scope = app.ApplicationServices
			.GetRequiredService<IServiceScopeFactory>().CreateScope();
		var service = scope.ServiceProvider;

		var context = service.GetRequiredService<ShopAppDbContext>();

		context.Database.Migrate();

		var userManager = scope.ServiceProvider
			.GetRequiredService<UserManager<User>>();

		var roleManager = scope.ServiceProvider
			.GetRequiredService<RoleManager<Role>>();

		if (!context.Roles.Any())
		{
			await roleManager.CreateAsync(new Role { Name = "admin" });
			await roleManager.CreateAsync(new Role { Name = "user" });
		}

		if (!context.Users.Any())
		{
			var user = new User
			{
				Email = "admin@email.com",
				UserName = "admin@email.com",
				EmailConfirmed = true,
			};

			var result = userManager.CreateAsync(user, "Admin+1111").Result;

			if (result.Succeeded)
			{
				await userManager.AddToRoleAsync(user, "admin");
			}
		}
	}
}
