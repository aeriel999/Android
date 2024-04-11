using Microsoft.EntityFrameworkCore;
using ShopApp.Entities;

namespace ShopApp.Persistence;

public class ShopAppDbContext : DbContext
{
	public ShopAppDbContext() : base() { }
	public ShopAppDbContext(DbContextOptions<ShopAppDbContext> options) : base(options) { }
	public DbSet<Category> Categories { get; set; }

	protected override void OnModelCreating(ModelBuilder builder)
	{
		base.OnModelCreating(builder);
	}
}
