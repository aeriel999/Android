using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using ShopApp.Entities;
using ShopApp.Entities.Identity;

namespace ShopApp.Persistence;

public class ShopAppDbContext : IdentityDbContext<User, Role, Guid>
{
	public ShopAppDbContext() : base() { }
	public ShopAppDbContext(DbContextOptions<ShopAppDbContext> options) : base(options) { }
	public DbSet<Category> Categories { get; set; }

	protected override void OnModelCreating(ModelBuilder builder)
	{
		base.OnModelCreating(builder);
	}
}
