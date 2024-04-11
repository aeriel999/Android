using ErrorOr;
using Microsoft.EntityFrameworkCore;
using ShopApp.Entities;
using ShopApp.Persistence;

namespace ShopApp.Repositories;

public class CategoryRepositories(ShopAppDbContext context)
{
	private readonly DbSet<Category> _dbSet = context.Set<Category>();

	public async Task<Category> CreateAsync(Category category)
	{
		await _dbSet.AddAsync(category);

		return category;
	}

	public async Task SaveAsync()
	{
		await context.SaveChangesAsync();
	}

	public async Task UpdateAsync(Category category)
	{
		await Task.Run
		(
		() =>
		{
			_dbSet.Attach(category);
			context.Entry(category).State = EntityState.Modified;
		});
	}

	public async Task<Category> GetByIdAsync(Guid id)
	{
		return await _dbSet.FindAsync(id);
	}

	public async Task DeleteAsync(Category entityToDelete)
	{
		await Task.Run
			(
				() =>
				{
					if (context.Entry(entityToDelete).State == EntityState.Detached)
					{
						_dbSet.Attach(entityToDelete);
					}
					_dbSet.Remove(entityToDelete);
				});
	}

	public async Task<List<Category>> GetListOfCategoriesAsync()
	{
		 return await _dbSet.ToListAsync();
	}
}
